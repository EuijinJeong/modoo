package com.example.modoo.service;

import com.example.modoo.dto.MemberRequestDto;
import com.example.modoo.dto.MemberResponseDto;
import com.example.modoo.dto.TokenDto;
import com.example.modoo.dto.TokenRequestDto;
import com.example.modoo.entity.Member;
import com.example.modoo.entity.RefreshToken;
import com.example.modoo.jwt.TokenProvider;
import com.example.modoo.repository.MemberRepository;
import com.example.modoo.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * AuthService 클래스는 회원가입, 로그인, 토큰 재발급과 같은 인증 관련 주요 서비스를 제공합니다.
 * 이 클래스는 사용자의 인증 정보를 처리하고, JWT 토큰을 생성 및 관리하는 역할을 담당합니다.
 *
 * 주요 기능:
 * - 회원가입(signup): 사용자로부터 받은 회원가입 요청 정보를 바탕으로 회원 정보를 생성하고 데이터베이스에 저장합니다.
 * - 로그인(login): 사용자의 로그인 요청을 검증하고, 성공적인 인증 후 JWT 토큰을 발급합니다.
 * - 토큰 재발급(reissue): 만료된 접근 토큰(Access Token)의 유효성을 갱신하기 위해 새로운 토큰을 발급합니다.
 *
 * 각 메소드는 @Transactional 어노테이션을 사용하여 데이터베이스 작업이 모두 성공적으로 완료되거나,
 * 실패 시 롤백을 보장합니다. 이를 통해 데이터 일관성과 안정성을 유지합니다.
 *
 * 이 서비스는 AuthenticationManagerBuilder를 사용하여 스프링 시큐리티의 인증 메커니즘을 통합하며,
 * 사용자 인증 정보의 유효성을 검증합니다.
 */

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final StoreService storeService;

    // 회원가입
    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }
        Member member = memberRequestDto.toMember(passwordEncoder);
        memberRepository.save(member);

        // 회원가입시 상점 정보를 생성하는 로직 아래에 작성해야함.
        storeService.createStore(member);

        return MemberResponseDto.of(member);
    }

    /**
     *
     * @param memberRequestDto
     * @return
     */
    @Transactional
    public TokenDto login(MemberRequestDto memberRequestDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 디버깅용
        RefreshToken savedToken = refreshTokenRepository.findByKey(authentication.getName()).orElse(null);
        if (savedToken != null) {
            System.out.println("Refresh Token 저장 성공: " + savedToken.getValue());
        } else {
            System.out.println("Refresh Token 저장 실패");
        }

        // 5. 토큰 발급
        return tokenDto;
    }

    // 토큰 재발급
    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDto;
    }
}
