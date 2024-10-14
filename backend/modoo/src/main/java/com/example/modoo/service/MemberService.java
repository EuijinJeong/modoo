package com.example.modoo.service;

import com.example.modoo.controller.SignUpRequest;
import com.example.modoo.entity.Member;
import com.example.modoo.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


// 애플리케이션의 비즈니스 로직을 담당하고 있음.
// 서비스 계층은 주로 컨트롤러와 리포지토리 사이의 중간 역할을 하며, 요청을 처리하는 로직을 포함
@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 이메일 중복 확인 메서드
    public boolean isEmailDuplicate(String email) {
        return memberRepository.existsByEmail(email); // 이메일 중복 여부 체크
    }

    @Transactional
    public void saveMember(SignUpRequest signUpRequest) {
        // 회원가입 로직을 구현하여 MySQL 데이터베이스에 사용자 추가
        Member member = new Member();
        member.setEmail(signUpRequest.getEmail());
        member.setPassword(passwordEncoder.encode(signUpRequest.getPassword())); // 비밀번호 해시 처리
        member.setFullName(signUpRequest.getFullName());
        member.setStudentId(signUpRequest.getStudentId());

        // 데이터베이스에 저장
        memberRepository.save(member);
    }
}

