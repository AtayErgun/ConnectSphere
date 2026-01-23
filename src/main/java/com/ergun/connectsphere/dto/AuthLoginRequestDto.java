package com.ergun.connectsphere.dto;

import lombok.Data;

@Data
public class AuthLoginRequestDto {
    private String email;
    private String password;
}