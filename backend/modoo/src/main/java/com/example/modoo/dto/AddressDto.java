package com.example.modoo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDto {
    private String recipient;
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String postalCode;
}
