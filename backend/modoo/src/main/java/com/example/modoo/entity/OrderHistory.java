package com.example.modoo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String impUid; // 아임포트 결제 고유 ID

    @Column(nullable = false)
    private int amount; // 결제 금액

    @Column(nullable = false)
    private String status; // 결제 상태 (예: "PAID", "CANCELLED")

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime cancelledAt;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)  // 회원 정보 연관
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")  // 상품 정보 연관
    private Product product;
}
