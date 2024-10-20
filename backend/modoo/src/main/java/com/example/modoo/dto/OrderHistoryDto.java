package com.example.modoo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderHistoryDto {
    private String userEmail;
    private Long productId;
    private int amount;
    private String impUid;
    private String orderDate;
}
