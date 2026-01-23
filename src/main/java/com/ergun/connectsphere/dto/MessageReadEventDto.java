package com.ergun.connectsphere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MessageReadEventDto {
    private Long messageId;
    private Long userId;
}
