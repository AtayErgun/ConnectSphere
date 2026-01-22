package com.ergun.connectsphere.dto;

import com.ergun.connectsphere.entity.UserEntity;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {

    private Long id;
    private String username;
    private String email;
    private String avatarUrl;

    public static UserResponseDto fromEntity(UserEntity user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}