package com.ergun.connectsphere.dto;

import lombok.Data;

@Data
public class AuthRegisterRequestDto {
    private String username;
    private String email;
    private String password;
}