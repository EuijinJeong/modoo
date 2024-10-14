package com.example.modoo.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "member_address")
@Getter
@Setter
public class MemberAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipient", nullable = false)
    private String recipient;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address_line_1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    // 기본 주소 여부를 나타내는 필드
    @Column(name = "isDeafult_address")
    private boolean isDefault;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 추가적인 필드가 필요할 경우 이곳에 추가 가능
}