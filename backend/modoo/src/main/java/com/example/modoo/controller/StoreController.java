package com.example.modoo.controller;

import com.example.modoo.dto.StoreDto;
import com.example.modoo.entity.Store;
import com.example.modoo.repository.StoreRepository;
import com.example.modoo.service.StoreService;
import com.example.modoo.service.UserEmailLookupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Store Controller 클래스는 Spring Boot 애플리케이션에서 RESTful API를 통해 상점(Store) 정보를 제공하는 역할을 합니다.
 * 이 클래스는 클라이언트로부터 HTTP 요청을 받아, 데이터베이스에서 상점 정보를 조회한 후, 클라이언트에게 응답으로 해당 정보를 반환합니다.
 */

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/stores")
public class StoreController {

    @Autowired
    private StoreService storeService;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserEmailLookupService userEmailLookupService;

    // 현재 사용자의 상점 정보를 조회
    @GetMapping("/current")
    public ResponseEntity<StoreDto> getStoreForCurrentUser() {
        try {
            StoreDto storeDto = storeService.getStoreForCurrentUser();
            System.out.println(storeDto + " 객체가 잘 반환되는지 확인하는 코드");
            return ResponseEntity.ok(storeDto);
        } catch (Exception e) {
            e.printStackTrace(); // 콘솔에 스택 트레이스 출력
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 현재 사용자의 상점 소개글을 업데이트
    @PostMapping("/update-store-note")
    public ResponseEntity<String> updateStoreNote(@RequestBody Store storeDetails) {
        String userEmail = userEmailLookupService.getCurrentUserEmail();

        Store store = storeRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Store not found"));

        store.setNote(storeDetails.getNote());
        storeRepository.save(store);

        return ResponseEntity.ok("Store note updated successfully");
    }

    // 특정 상점 정보를 조회
    @GetMapping("/{storeId}")
    public ResponseEntity<StoreDto> getStoreInfo(@PathVariable Long storeId) {
        StoreDto storeDto = storeService.getStoreInfoById(storeId);
        System.out.println("http 요청 잘 도착함.");
        return ResponseEntity.ok(storeDto);
    }
}

