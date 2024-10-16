package com.example.modoo.controller;

import com.example.modoo.service.LogoutService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로그아웃을 컨트롤하는 컨트롤러이다.
 *
 * @author jeong-uijin
 */
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api")
public class LogoutController {

    @Autowired
    private LogoutService logoutService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        try {
            logoutService.logout(request); // LogoutService의 메서드 호출
            return new ResponseEntity<>("로그아웃 완료", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("로그아웃 중 오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
