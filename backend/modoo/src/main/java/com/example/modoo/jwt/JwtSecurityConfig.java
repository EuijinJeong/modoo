package com.example.modoo.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SecurityConfigurerAdapter <DefaultSecurityFilterChain, HttpSecurity> 인터페이스를 구현하는 구현체이다.
 * 여기서 직접 만든 JwtFilter를 Security Filter 앞에 추가함
 */

// 직접 만든 TokenProvider 와 JwtFilter 를 SecurityConfig 에 적용할 때 사용

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final TokenProvider tokenProvider;

    // TokenProvider 를 주입받아서 JwtFilter 를 통해 Security 로직에 필터를 등록
    @Override
    public void configure(HttpSecurity http) {
        JwtFilter customFilter = new JwtFilter(tokenProvider);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
