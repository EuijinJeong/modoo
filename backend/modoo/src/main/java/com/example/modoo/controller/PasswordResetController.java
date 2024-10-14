package com.example.modoo.controller;

import com.example.modoo.dto.PasswordResetRequestDto;
import com.example.modoo.dto.PasswordUpdateRequestDto;
import com.example.modoo.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/password")
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    /**
     * 비밀번호 재설정 이메일 요청 보내는 컨트롤러
     * @param request
     * @return
     */
    @PostMapping("/reset-link")
    public ResponseEntity<String> sendResetLink(@RequestBody PasswordResetRequestDto request) {
        String email = request.getEmail();
        try {
            passwordResetService.resetPassword(email);
            return ResponseEntity.ok("Password reset link sent to " + email);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    // 비밀번호 재설정 처리
    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordUpdateRequestDto request) {
        String email = request.getEmail();
        String newPassword = request.getNewPassword();

        try {
            passwordResetService.updatePassword(email, newPassword);
            return ResponseEntity.ok("Password has been reset successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

}
