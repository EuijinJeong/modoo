package com.example.modoo.service;

import com.example.modoo.dto.MemberResponseDto;
import com.example.modoo.dto.StoreDto;
import com.example.modoo.entity.Member;
import com.example.modoo.entity.Store;
import com.example.modoo.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class StoreService {

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserEmailLookupService userEmailLookupService;

    @Autowired
    private ProductService productService;

    @Transactional
    public Store createStore(Member member) {

        // 사용자당 하나의 상점만 생성이 되도록 로직 아래에 추가
        if (storeRepository.existsByEmail(member.getEmail())) {
            throw new RuntimeException("이미 상점이 생성된 사용자입니다");
        }

        Store store = new Store();
        store.setEmail(member.getEmail());
        store.setName(member.getStudentId()); // 상점 이름 초기값으로 학번 설정
        store.setNote("상점 소개글을 입력해 주세요.");
        store.setMember(member);

        // Member 엔티티에도 Store 설정
        member.setStore(store);

        return storeRepository.save(store);
    }

    // 수정해야함
    @Transactional(readOnly = true)
    public StoreDto getStoreForCurrentUser() {
        String email = userEmailLookupService.getCurrentUserEmail();
        System.out.println("Current user email: " + email);

        Store store = storeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        System.out.println("Store retrieved: " + store);

        return convertToDto(store);
    }

    public StoreDto getStoreInfoById(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found"));
        return convertToDto(store);
    }

    private StoreDto convertToDto(Store store) {
        StoreDto storeDto = new StoreDto();
        storeDto.setId(store.getId());
        storeDto.setName(store.getName());
        storeDto.setEmail(store.getEmail());
        storeDto.setNote(store.getNote());
        storeDto.setMember(MemberResponseDto.of(store.getMember()));
        storeDto.setProducts(store.getProducts().stream()
                .map(productService::convertToDto)
                .collect(Collectors.toList()));
        storeDto.setProductsCount(store.getProducts().size());
        return storeDto;
    }
}
