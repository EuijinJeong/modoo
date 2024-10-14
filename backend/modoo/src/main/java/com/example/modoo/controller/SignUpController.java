package com.example.modoo.controller;

import com.example.modoo.dto.MemberRequestDto;
import com.example.modoo.dto.MemberResponseDto;
import com.example.modoo.service.AuthService;
import com.example.modoo.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
@RequiredArgsConstructor
public class SignUpController {

    @Autowired
    private final AuthService authService;

    @Autowired
    private MemberService memberService;

    @GetMapping("/check-email-duplicate")
    public ResponseEntity<Map<String, Boolean>> checkEmailDuplicate(@RequestParam String email) {
        boolean isDuplicated = memberService.isEmailDuplicate(email);  // 서비스에서 이메일 중복 여부 확인
        Map<String, Boolean> response = new HashMap<>();
        response.put("isDuplicate", isDuplicated);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원가입 요청 컨트롤러.
     * @param signUpRequest
     * @return
     */
    @PostMapping("/signup") // api 요청 엔드포인트
    public ResponseEntity<Object> signUp(@RequestBody SignUpRequest signUpRequest) {
        try {
            // 사용자가 입력한 정보를 MemberRequestDto로 옮김
            MemberRequestDto memberRequestDto = new MemberRequestDto(
                    signUpRequest.getEmail(),
                    signUpRequest.getPassword(),
                    signUpRequest.getFullName(),
                    signUpRequest.getStudentId()
            );
            MemberResponseDto memberResponseDto = authService.signup(memberRequestDto);
            return ResponseEntity.ok(memberResponseDto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}