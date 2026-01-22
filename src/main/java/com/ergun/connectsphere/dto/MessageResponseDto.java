package com.ergun.connectsphere.dto;


import com.ergun.connectsphere.MessageType;
import com.ergun.connectsphere.entity.MessageEntity;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MessageResponseDto {

    private Long id;
    private String content;
    private LocalDateTime timestamp;
    private Long senderId;
    private Long groupId;
    private MessageType messageType;
    private String attachmentUrl;

    public static MessageResponseDto fromEntity(MessageEntity message) {
        return MessageResponseDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .timestamp(message.getTimestamp())
                .senderId(message.getSender().getId())
                .groupId(message.getGroup().getId())
                .messageType(message.getMessageType())
                .attachmentUrl(message.getAttachmentUrl())
                .build();
    }
}
