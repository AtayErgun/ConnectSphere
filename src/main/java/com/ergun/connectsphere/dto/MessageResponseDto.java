package com.ergun.connectsphere.dto;


import com.ergun.connectsphere.MessageType;
import com.ergun.connectsphere.entity.MessageEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDto {

    private Long id;
    private String content;
    private MessageType messageType;
    private String attachmentUrl;
    private LocalDateTime timestamp;
    private Long senderId;
    private String senderUsername;
    private boolean isDeleted;
    private List<ReactionDto> reactions;

    public static MessageResponseDto fromEntity(MessageEntity message) {
        return MessageResponseDto.builder()
                .id(message.getId())
                .content(message.getContent())
                .messageType(message.getMessageType())
                .attachmentUrl(message.getAttachmentUrl())
                .timestamp(message.getTimestamp())
                .senderId(message.getSender().getId())
                .isDeleted(message.isDeleted())
                .senderUsername(message.getSender().getUsername())
                // 🛡️ NULL KONTROLÜ EKLENDİ: Eğer reactions listesi null ise hata vermez, boş liste döner.
                .reactions(
                        message.getReactions() != null ?
                                message.getReactions().stream()
                                        .map(r -> ReactionDto.builder()
                                                .userId(r.getUser().getId())
                                                .emoji(r.getEmojiCode())
                                                .build())
                                        .toList()
                                : new ArrayList<>()
                )
                .build();
    }
}