package com.example.modoo.service;

import com.example.modoo.dto.MemberRequestDto;
import com.example.modoo.dto.MemberResponseDto;
import com.example.modoo.member.dao.TokenDao;
import com.example.modoo.dto.TokenRequestDto;
import com.example.modoo.entity.Member;
import com.example.modoo.entity.RefreshToken;
import com.example.modoo.jwt.TokenProvider;
import com.example.modoo.repository.MemberRepository;
import com.example.modoo.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


/**
 * {@code AuthService} 클래스는 회원가입, 로그인, 토큰 재발급과 같은 인증 관련 주요 서비스를 제공합니다.
 * <p>
 * 이 클래스는 사용자의 인증 정보를 처리하고, JWT 토큰을 생성 및 관리하는 역할을 담당합니다.
 *
 * <h2>주요 기능:</h2>
 * <ul>
 * <li><b>회원가입(signup):</b> 사용자로부터 받은 회원가입 요청 정보를 바탕으로 회원 정보를 생성하고 데이터베이스에 저장합니다.</li>
 * <li><b>로그인(login):</b> 사용자의 로그인 요청을 검증하고, 성공적인 인증 후 JWT 토큰을 발급합니다.</li>
 * <li><b>토큰 재발급(reissue):</b> 만료된 접근 토큰(Access Token)의 유효성을 갱신하기 위해 새로운 토큰을 발급합니다.</li>
 * </ul>
 *
 * <p>
 * 각 메소드는 {@code @Transactional} 어노테이션을 사용하여 데이터베이스 작업이 모두 성공적으로 완료되거나,
 * 실패 시 롤백을 보장합니다. 이를 통해 데이터 일관성과 안정성을 유지합니다.
 * <p>
 * 이 서비스는 {@code AuthenticationManagerBuilder}를 사용하여 스프링 시큐리티의 인증 메커니즘을 통합하며,
 * 사용자 인증 정보의 유효성을 검증합니다.
 *
 * @see org.springframework.transaction.annotation.Transactional
 * @see org.springframework.security.authentication.AuthenticationManagerBuilder
 */

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
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
     * 사용자의 로그인을 처리하는 메소드.
     * @param memberRequestDto
     * @return
     */
    @Transactional
    public TokenDao login(MemberRequestDto memberRequestDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // TODO 비밀번호 틀렸을 경우 예외처리 해야함.
        TokenDao tokenDao = null;
        try {
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
             tokenDao = tokenProvider.generateToken(authentication);

            // 4. RefreshToken 저장
            try {
                RefreshToken refreshToken = RefreshToken.builder()
                        .key(authentication.getName())
                        .value(tokenDao.getRefreshToken())
                        .build();
                refreshTokenRepository.save(refreshToken);
            } catch (IllegalArgumentException iae) {
                logger.warn(iae.getMessage());
                throw new IllegalArgumentException("서버상의 이유로 토큰 발급이 불가합니다. 관리자에게 문의 바랍니다.");
            }

            RefreshToken savedToken = refreshTokenRepository.findByKey(authentication.getName()).orElse(null);
            if (savedToken != null) {
//                System.out.println("Refresh Token 저장 성공: " + savedToken.getValue());
                logger.info("Refresh Token 저장 성공: " + savedToken.getValue());
            } else {
//                System.out.println("Refresh Token 저장 실패");
                logger.info("Refresh Token 저장 실패");
            }
        } catch (BadCredentialsException bce) {
            // 아이디 또는 비밀번호가 틀렸을 경우
            logger.error("Login failed for user '{}'. Incorrect password or username.");
            throw new RuntimeException("아이디 또는 비밀번호가 일치하지 않습니다.");
        }
        // 계정이 비활성화 된 경우
        catch (DisabledException de) {
            logger.warn("Disabled account access attempt. UserID: {}", memberRequestDto.getEmail());
            throw new RuntimeException("계정이 비활성화되었습니다.");
        }
        // 계정이 잠긴 경우
        catch (LockedException le) {
            logger.warn("locked account access attempt. UserID: {}", memberRequestDto.getEmail());
            throw new RuntimeException("계정이 잠겨있습니다.");
        }

        logger.info("Token issuance successful.");
        // 5. 토큰 발급
        return tokenDao;
    }

    // 토큰 재발급
    @Transactional
    public TokenDao reissue(TokenRequestDto tokenRequestDto) {
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
        TokenDao tokenDao = tokenProvider.generateToken(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDao.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDao;
    }
}
