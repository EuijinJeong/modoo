package com.example.modoo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

/**
 * 프론트엔드, 백엔드를 구분지어서 개발하거나, 서로 다른 Server 환경에서 자원을 공유할 때,
 * Cors설정이 안 되어있으면 오류가 발생한다. 이를 방지하기 위해 Cors 설정을 해주어야 한다.
 *
 * Credentials: 처리방식을 설정해 준다. 기본적으로 요청에 대한 응답으로 json타입이 나간다. 이를 Javascript에서 처리할 수 있게 해 준다. 이를 통해 프런트엔드에서 처리된 응답을 뷰에 맞게 설정해 줄 수 있다.
 * Origin: ("*")는 모든 출처를 허용한다는 뜻이다.
 * Header: ("*")는 모든 헤더를 허용한다는 뜻이다.
 * Method: Get/Post/Delete.. 등 요청을 허용하는 방식, ("*")는 모든 방식을 허용한다는 뜻이다.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // 쿠키를 지원하려면 true로 설정
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000")); // 허용할 오리진 설정
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // 허용할 HTTP 메서드 설정
        config.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더 허용
        config.setMaxAge(3600L); // 브라우저 캐싱 시간 (초 단위)

        source.registerCorsConfiguration("/api/**", config); // CORS를 적용할 URL 패턴
        return new CorsFilter(source);
    }
}
