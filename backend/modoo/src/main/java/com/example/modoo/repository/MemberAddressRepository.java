package com.example.modoo.repository;

import com.example.modoo.entity.MemberAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberAddressRepository extends JpaRepository<MemberAddress, Long> {
    // memberId와 isDefault 필드를 통해 기본 주소 조회
    MemberAddress findByMemberIdAndIsDefault(Long memberId, boolean isDefault);

    // 특정 회원의 모든 주소 리스트 조회
    List<MemberAddress> findByMemberId(Long memberId);
}