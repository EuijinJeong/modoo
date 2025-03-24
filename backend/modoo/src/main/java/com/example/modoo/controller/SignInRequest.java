package com.example.modoo.controller;

// 클라이언트로부터 전송된 JSON 데이터를 자바 객체로 변환해주는 역할
// 클라이언트와 서버 간의 데이터 교환을 위한 중간 객체 역할

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignInRequest {
    private String email;
    private String password;

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}