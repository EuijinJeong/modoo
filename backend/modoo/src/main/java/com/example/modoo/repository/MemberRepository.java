package com.example.modoo.repository;

import com.example.modoo.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


// 데이터베이스와의 상호 작용을 담당합니다. CRUD(Create, Read, Update, Delete) 작업을 수행할 메소드를 제공합니다.
public interface MemberRepository extends JpaRepository<Member, Long> {
    // 이메일을 기준으로 사용자를 찾는 메서드
    // Email로 로그인을 하기 때문에 중복 가입 방지와 존재여부를 파악하는 메서드를 추가
    Optional<Member> findByEmail(String email);
    boolean existsByEmail(String email);

}
