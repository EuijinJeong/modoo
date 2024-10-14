package com.example.modoo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "order")
@Getter
@Setter
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")  // 회원 정보 연관
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")  // 상품 정보 연관
    private Product product;

    private int price;  // 결제 금액

    // FIXME: 이거 고쳐야할 수도 있음
    private String paymentStatus;  // 결제 상태 (예: '완료', '취소' 등)
    private LocalDateTime orderDate;

    @OneToOne
    @JoinColumn(name = "member_address_id")
    private MemberAddress memberAddress;  // 배송 정보
}
