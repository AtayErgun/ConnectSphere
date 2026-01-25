package com.ergun.connectsphere.service;


import com.ergun.connectsphere.MessageType;
import com.ergun.connectsphere.dto.MessageReadEventDto;
import com.ergun.connectsphere.dto.MessageResponseDto;
import com.ergun.connectsphere.entity.*;
import com.ergun.connectsphere.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatGroupRepository chatGroupRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final MessageReadStatusRepository messageReadStatusRepository;
    private final MessageReactionRepository reactionRepository;

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

    public long getUnreadCount(Long groupId, Long userId) {
        return messageRepository.countUnreadMessages(groupId, userId);
    }

    public void addReaction(Long messageId, Long userId, String emoji) {

        MessageEntity message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<MessageReactionEntity> existing =
                reactionRepository.findByMessageIdAndUserIdAndEmojiCode(
                        messageId, userId, emoji
                );

        String type;

        if (existing.isPresent()) {
            reactionRepository.delete(existing.get());
            type = "REMOVE";
        } else {
            MessageReactionEntity reaction = MessageReactionEntity.builder()
                    .message(message)
                    .user(user)
                    .emojiCode(emoji)
                    .build();
            reactionRepository.save(reaction);
            type = "ADD";
        }

        messagingTemplate.convertAndSend(
                "/topic/group/" + message.getGroup().getId() + "/reactions",
                Map.of(
                        "messageId", messageId,
                        "userId", userId,
                        "emoji", emoji,
                        "type", type
                )
        );
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

    public void editMessage(Long messageId, Long userId, String newContent) {

        MessageEntity message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!message.getSender().getId().equals(userId)) {
            throw new RuntimeException("You can only edit your own message");
        }

        if (message.isDeleted()) {
            throw new RuntimeException("Deleted message cannot be edited");
        }

        message.setContent(newContent);
        messageRepository.save(message);

        messagingTemplate.convertAndSend(
                "/topic/group/" + message.getGroup().getId() + "/updates",
                Map.of(
                        "messageId", messageId,
                        "content", newContent,
                        "type", "EDIT"
                )
        );
    }

    // 🗑️ DELETE MESSAGE (Soft delete)
    public void deleteMessage(Long messageId, Long userId) {

        MessageEntity message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found"));

        if (!message.getSender().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own message");
        }

        message.setDeleted(true);
        messageRepository.save(message);

        messagingTemplate.convertAndSend(
                "/topic/group/" + message.getGroup().getId() + "/updates",
                Map.of(
                        "messageId", messageId,
                        "type", "DELETE"
                )
        );
    }
}