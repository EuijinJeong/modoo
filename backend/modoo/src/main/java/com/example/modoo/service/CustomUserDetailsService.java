package com.example.modoo.service;

import com.example.modoo.entity.Member;
import com.example.modoo.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * CustomUserDetailsService 클래스는 Spring Security에서 사용자 인증을 처리하는 UserDetailsService 인터페이스를 구현한 클래스입니다.
 * 이 클래스의 주요 역할은 주어진 사용자 이름(이 경우 이메일 주소)에 따라 사용자 세부 정보를 로드하여
 * Spring Security가 인증 과정을 수행할 수 있도록 하는 것입니다.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException(username + " -> 데이터베이스에서 찾을 수 없습니다."));
    }

    // DB에 User 값이 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Member member) {
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getAuthority().toString());

        return new User(
                member.getEmail(), // 사용자 아이디를 리턴함.
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );
    }
}
