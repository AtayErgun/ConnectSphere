package com.ergun.connectsphere.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypingEventDto {

    private Long groupId;
    private Long userId;
    private String username;
    private boolean isTyping;
}