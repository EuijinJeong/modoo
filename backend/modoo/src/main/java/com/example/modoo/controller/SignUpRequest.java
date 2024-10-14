package com.example.modoo.controller;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SignUpRequest {
    private String email;
    private String password;
    private String fullName;
    private String studentId;
}
