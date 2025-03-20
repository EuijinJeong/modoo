package com.example.modoo.member.controller;

import com.example.modoo.dto.MemberRequestDto;
import com.example.modoo.dto.TokenDto;
import com.example.modoo.member.vo.SigninVO;
import com.example.modoo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    private final AuthService authService;

    /**
     * 사용자 로그인을 처리하고 JWT 토큰을 발급합니다.
     * <p>
     * 입력받은 이메일과 비밀번호를 검증하고, 성공적인 인증 후 JWT 토큰을 {@link TokenDto} 형태로 반환합니다.
     * </p>
     * <br>
     * @param signinVO 로그인 요청 정보를 담은 객체입니다. 이메일과 비밀번호를 포함합니다.
     * @return 로그인 성공 시 JWT 토큰을 담은 {@link TokenDto} 객체를 {@link ResponseEntity}에 담아 반환합니다.
     * @throws org.springframework.security.authentication.BadCredentialsException 잘못된 비밀번호를 입력한 경우 발생합니다.
     * @throws org.springframework.security.authentication.DisabledException 비활성화된 계정으로 로그인한 경우 발생합니다.
     * @throws org.springframework.security.authentication.LockedException 잠긴 계정으로 로그인한 경우 발생합니다.
     * @see TokenDto
     * @see SigninVO
     */
    @PostMapping("/signIn")
    public ResponseEntity<Object> login (@RequestBody SigninVO signinVO) {
        String email = signinVO.getEmail();
        String password = signinVO.getPassword();

        // 사용자 로그인 처리
        TokenDto tokenDto = authService.login(new MemberRequestDto(email, password));

        return ResponseEntity.ok(tokenDto);
    }
}
