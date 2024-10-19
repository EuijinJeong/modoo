package com.example.modoo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDto {

    private Long memberId;
    private Long productId;
    private int amount;
    private String impUid;
    private AddressDto address;
}
