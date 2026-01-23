package com.ergun.connectsphere.service;


import com.ergun.connectsphere.MessageType;
import com.ergun.connectsphere.dto.MessageReadEventDto;
import com.ergun.connectsphere.dto.MessageResponseDto;
import com.ergun.connectsphere.entity.ChatGroupEntity;
import com.ergun.connectsphere.entity.MessageEntity;
import com.ergun.connectsphere.entity.MessageReadStatusEntity;
import com.ergun.connectsphere.entity.UserEntity;
import com.ergun.connectsphere.repository.ChatGroupRepository;
import com.ergun.connectsphere.repository.MessageReadStatusRepository;
import com.ergun.connectsphere.repository.MessageRepository;
import com.ergun.connectsphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatGroupRepository chatGroupRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageReadStatusRepository messageReadStatusRepository;


    public MessageResponseDto sendMessage(
            Long senderId,
            Long groupId,
            String content,
            MessageType messageType,
            String attachmentUrl
    ) {
        UserEntity sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));

        ChatGroupEntity group = chatGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        MessageEntity message = MessageEntity.builder()
                .sender(sender)
                .group(group)
                .content(content)
                .messageType(messageType)
                .attachmentUrl(attachmentUrl)
                .timestamp(LocalDateTime.now())
                .build();

        MessageEntity savedMessage = messageRepository.save(message);

        MessageResponseDto responseDto = MessageResponseDto.fromEntity(savedMessage);

        // 🔴 WebSocket publish
        messagingTemplate.convertAndSend(
                "/topic/group/" + groupId,
                responseDto
        );

        return responseDto;
    }

    public List<MessageResponseDto> getMessagesByGroup(Long groupId) {

        ChatGroupEntity group = chatGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));

        return messageRepository.findByGroupOrderByTimestampAsc(group)
                .stream()
                .map(MessageResponseDto::fromEntity)
                .toList();
    }

    public void markMessageAsRead(Long messageId, Long userId) {

        MessageEntity message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Aynı kullanıcı aynı mesajı tekrar okumasın
        boolean alreadyRead = messageReadStatusRepository
                .existsByMessageAndUser(message, user);

        if (alreadyRead) {
            return;
        }

        MessageReadStatusEntity readStatus = MessageReadStatusEntity.builder()
                .message(message)
                .user(user)
                .readAt(LocalDateTime.now())
                .build();

        messageReadStatusRepository.save(readStatus);

        // 🔴 WebSocket publish (okundu bilgisi)
        messagingTemplate.convertAndSend(
                "/topic/group/" + message.getGroup().getId() + "/reads",
                new MessageReadEventDto(messageId, userId)
        );
    }
}