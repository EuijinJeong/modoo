package com.skuniv.modoo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentVerifiedDto {
    private String impUid;
    private Integer amount;
    private Long productId;
    private String userEmail;
    private String orderDate;
}
