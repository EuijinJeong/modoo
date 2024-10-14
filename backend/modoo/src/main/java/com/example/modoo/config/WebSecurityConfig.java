package com.example.modoo.config;

//import com.example.modoo.jwt.CustomUserDetailsService;
import com.example.modoo.jwt.JwtFilter;
import com.example.modoo.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final TokenProvider tokenProvider;
    private final CustomAccessDeniedHandler accessDeniedHandler;
//    private final CustomLoginSuccessHandler loginSuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())  // REST API이므로 CSRF 보안을 사용하지 않음
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/SignUpPage").permitAll()
                        .requestMatchers("/api/signup").permitAll()
                        .requestMatchers("/api/signIn").permitAll()
                        .requestMatchers("/api/user/**").permitAll()
                        .requestMatchers("static/images/**").permitAll()
                        .requestMatchers("/api/password/reset-link").permitAll()  // 인증 없이 접근 가능
                        .requestMatchers("/api/password/reset").permitAll()
                        .requestMatchers("/api/check-email-duplicate").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(e -> e
                        .accessDeniedHandler(accessDeniedHandler)
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                // 아래 코드에서 오류 발생, 주석처리 했더니 401 오류 없어지면서 정상 작동함. 나중에 디버깅 필요
//                .formLogin(form -> form
//                        .loginPage("/signIn")
//                        .successHandler(loginSuccessHandler) // 로그인 성공 핸들러 추가
//                )
                .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // 정적 리소스 spring security 대상에서 제외
        return (web) -> web
                // 정적 리소스(Spring 기본 리소스 경로들)를 인증 대상에서 제외
                .ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                // 별도로 /images/** 경로를 인증 대상에서 제외
                .and()
                .ignoring()
                .requestMatchers("/images/**");
    }

}
