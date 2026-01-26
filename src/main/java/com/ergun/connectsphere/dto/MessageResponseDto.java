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

    // 🔁 Reply info
    private Long parentMessageId;
    private String parentMessageContent;
    private String parentMessageSender;

    private boolean isDeleted;
    private List<ReactionDto> reactions;

    public static MessageResponseDto fromEntity(MessageEntity message) {

        MessageEntity parent = message.getParentMessage();

        return MessageResponseDto.builder()
                .id(message.getId())
                .content(message.isDeleted() ? null : message.getContent())
                .messageType(message.getMessageType())
                .attachmentUrl(message.getAttachmentUrl())
                .timestamp(message.getTimestamp())
                .senderId(message.getSender().getId())
                .senderUsername(message.getSender().getUsername())

                // 🔁 Reply mapping
                .parentMessageId(parent != null ? parent.getId() : null)
                .parentMessageContent(
                        parent != null && !parent.isDeleted()
                                ? parent.getContent()
                                : null
                )
                .parentMessageSender(
                        parent != null
                                ? parent.getSender().getUsername()
                                : null
                )

                .isDeleted(message.isDeleted())
                .reactions(
                        message.getReactions()
                                .stream()
                                .map(r -> ReactionDto.builder()
                                        .userId(r.getUser().getId())
                                        .emoji(r.getEmojiCode())
                                        .build())
                                .toList()
                )
                .build();
    }
}