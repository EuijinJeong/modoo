package com.example.modoo.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * 현재 인증된 사용자의 이메일을 가져오는 역할을 함
 */

@Service
public class UserEmailLookupService {

    // 현재 로그인한 인증된 사용자의 이메일 정보를 가져오게 하는 메소드
    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername(); // 인증된 이메일 정보를 반환
        }
        throw new IllegalStateException("현재 로그인된 사용자 정보를 찾을 수 없습니다.");
    }
}
