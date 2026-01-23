package com.ergun.connectsphere.dto;

import com.ergun.connectsphere.MessageType;
import lombok.Data;

@Data
public class MessageSendRequestDto {

    private Long senderId;
    private Long groupId;
    private String content;
    private MessageType messageType;
    private String attachmentUrl;
}