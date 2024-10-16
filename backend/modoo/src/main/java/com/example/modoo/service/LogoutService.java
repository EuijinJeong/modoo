package com.example.modoo.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class LogoutService {
    public void logout(HttpServletRequest request) {
        // Authorization 헤더에서 JWT 토큰을 추출
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            // "Bearer " 부분 제거하여 실제 토큰만 추출
            token = token.substring(7);

            // 여기서 토큰을 블랙리스트에 추가하거나, 캐시를 사용해 무효화 처리
            // 예시: tokenBlacklistService.addToBlacklist(token);
            System.out.println("로그아웃 처리된 토큰: " + token);
        } else {
            System.out.println("유효한 토큰이 제공되지 않았습니다.");
        }
    }
}
