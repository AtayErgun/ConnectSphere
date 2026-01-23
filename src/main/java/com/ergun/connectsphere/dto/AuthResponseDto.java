package com.ergun.connectsphere.dto;

import com.ergun.connectsphere.entity.UserEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponseDto {
    private Long id;
    private String username;
    private String email;

    public static AuthResponseDto fromEntity(UserEntity user) {
        return AuthResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}