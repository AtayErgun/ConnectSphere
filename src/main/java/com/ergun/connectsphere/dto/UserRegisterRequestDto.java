package com.ergun.connectsphere.dto;

import lombok.Data;

@Data
public class UserRegisterRequestDto {

    private String username;
    private String email;
    private String password;
}