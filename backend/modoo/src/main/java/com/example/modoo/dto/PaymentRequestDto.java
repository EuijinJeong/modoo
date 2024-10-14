package com.example.modoo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentRequestDto {

    private MemberResponseDto member;
    private ProductDto product;
    private int price;
    private AddressDto address;
}
