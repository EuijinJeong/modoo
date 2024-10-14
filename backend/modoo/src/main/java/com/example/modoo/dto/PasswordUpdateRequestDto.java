package com.example.modoo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordUpdateRequestDto {
    private String email;
    private String newPassword;
}
