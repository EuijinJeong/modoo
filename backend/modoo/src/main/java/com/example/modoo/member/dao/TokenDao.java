package com.example.modoo.member.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
@Builder
@Data
@AllArgsConstructor
public class TokenDao {
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
