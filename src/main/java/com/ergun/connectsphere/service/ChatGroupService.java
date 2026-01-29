package com.ergun.connectsphere.service;


import com.ergun.connectsphere.dto.ChatGroupCreateRequestDto;
import com.ergun.connectsphere.dto.ChatGroupResponseDto;
import com.ergun.connectsphere.dto.ChatGroupSummaryDto;
import com.ergun.connectsphere.entity.ChatGroupEntity;
import com.ergun.connectsphere.entity.UserEntity;
import com.ergun.connectsphere.repository.ChatGroupRepository;
import com.ergun.connectsphere.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatGroupService {

    private final ChatGroupRepository chatGroupRepository;
    private final MessageService messageService;
    private final UserRepository userRepository;

    // 🟢 YENİ: İki kişi arasında DM oluşturur veya olanı getirir
    @Transactional
    public ChatGroupResponseDto createOrGetPrivateChat(Long user1Id, Long user2Id) {
        // 1. Önce bu iki kişi arasında zaten bir DM var mı kontrol et
        Optional<ChatGroupEntity> existingChat =
                chatGroupRepository.findPrivateChatBetweenUsers(user1Id, user2Id);

        if (existingChat.isPresent()) {
            return ChatGroupResponseDto.fromEntity(existingChat.get());
        }

        // 2. Yoksa yeni bir özel grup (DM) oluştur
        UserEntity user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("User 1 not found"));
        UserEntity user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User 2 not found"));

        ChatGroupEntity dmGroup = ChatGroupEntity.builder()
                .name(user1.getUsername() + " & " + user2.getUsername())
                .description("Direkt Mesaj")
                .isPrivate(true) // 🔒 Özel sohbet olduğunu işaretle
                .creator(user1)
                .members(new ArrayList<>())
                .build();

        dmGroup.getMembers().add(user1);
        dmGroup.getMembers().add(user2);

        ChatGroupEntity savedDm = chatGroupRepository.save(dmGroup);
        return ChatGroupResponseDto.fromEntity(savedDm);
    }

    public ChatGroupResponseDto createGroup(ChatGroupCreateRequestDto request) {
        UserEntity creator = userRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        ChatGroupEntity group = ChatGroupEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .isPrivate(false) // 👥 Normal grup
                .creator(creator)
                .members(new ArrayList<>())
                .build();

        group.getMembers().add(creator);
        ChatGroupEntity savedGroup = chatGroupRepository.save(group);
        return ChatGroupResponseDto.fromEntity(savedGroup);
    }

    public List<ChatGroupSummaryDto> getGroupSummaries(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Kullanıcının üye olduğu tüm grupları (Normal + DM) çeker
        return chatGroupRepository.findByMembersContaining(user)
                .stream()
                .map(group -> ChatGroupSummaryDto.builder()
                        .groupId(group.getId())
                        .memberIds(group.getMembers().stream().map(UserEntity::getId).toList())
                        .groupName(group.getName())
                        .unreadCount(messageService.getUnreadCount(group.getId(), userId))
                        .build()
                )
                .toList();
    }

    // Mevcut diğer metodlar (getAllGroups, addMemberToGroup) aynen kalabilir...
    public List<ChatGroupResponseDto> getAllGroups() {
        return chatGroupRepository.findAll()
                .stream()
                .map(ChatGroupResponseDto::fromEntity)
                .toList();
    }

    public ChatGroupResponseDto addMemberToGroup(Long groupId, Long userId) {
        ChatGroupEntity group = chatGroupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!group.getMembers().contains(user)) {
            group.getMembers().add(user);
        }
        ChatGroupEntity updatedGroup = chatGroupRepository.save(group);
        return ChatGroupResponseDto.fromEntity(updatedGroup);
    }
}
