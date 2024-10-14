package com.example.modoo.repository;

import com.example.modoo.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository <Store, Long> {

    // member와 store를 연결하기 위해 아래와 같은 코드 작성함.
    Optional<Store> findByEmail(String email);
    boolean existsByEmail(String email);

    // Member ID로 Store 찾기
    Optional<Store> findByMemberId(Long memberId);

    // Store ID 를 통해서 해당 판매자 찾기
    Optional<Store> findById(Long id);
}
