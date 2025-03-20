package com.example.modoo.member.controller;

import com.example.modoo.controller.SignInRequest;
import com.example.modoo.dto.MemberRequestDto;
import com.example.modoo.dto.TokenDto;
import com.example.modoo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * SignInController 클래스는 사용자의 로그인 요청을 처리하는 컨트롤러입니다. 이 컨트롤러는 CORS 정책을 통해
 * 'http://localhost:3000' 도메인에서 발생하는 요청만을 수락합니다. 모든 메서드는 '/api' 경로 아래에서 작동하며,
 * 이 클래스는 생성자 주입 방식을 사용하여 AuthService의 의존성을 관리합니다.
 *
 * 주요 메서드:
 * - login(@RequestBody SignInRequest signInRequest): 이 메서드는 POST 요청을 통해 '/signIn' 경로로 들어오는
 *   로그인 요청을 처리합니다. 요청 본문에서 받은 SignInRequest 객체에서 이메일과 비밀번호를 추출하여 AuthService에 전달하고,
 *   로그인 처리 결과로 생성된 토큰을 TokenDto 객체로 반환합니다.
 *
 * 사용 방법:
 * - 사용자는 로그인 폼에 이메일과 비밀번호를 입력하고, 이 데이터는 JSON 형태로 서버로 전송됩니다.
 * - 서버는 이 정보를 받아 로그인을 시도하고, 성공적으로 로그인되면 토큰 정보를 담은 객체를 JSON 형태로 반환합니다.
 * - 반환된 토큰은 클라이언트에서 이후의 요청에 사용되어 사용자 인증을 유지하는 데 사용됩니다.
 *
 * @author jeong-uijin
 */

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
@RequiredArgsConstructor
public class SignInController {

    private final AuthService authService;

    @PostMapping("/signIn") // POST 요청을 처리할 엔드포인트 지정
    public ResponseEntity<Object> login (@RequestBody SignInRequest signInRequest) {
        // 로그인 요청을 처리하는 메서드
        String email = signInRequest.getEmail();
        String password = signInRequest.getPassword();

        // AuthService를 통해 로그인 처리 및 토큰 생성
        TokenDto tokenDto = authService.login(new MemberRequestDto(email, password));

        // 토큰을 응답으로 반환
        return ResponseEntity.ok(tokenDto);
    }
}
