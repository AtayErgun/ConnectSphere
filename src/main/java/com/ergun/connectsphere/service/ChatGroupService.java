package com.ergun.connectsphere.service;


import com.ergun.connectsphere.dto.ChatGroupCreateRequestDto;
import com.ergun.connectsphere.dto.ChatGroupResponseDto;
import com.ergun.connectsphere.entity.ChatGroupEntity;
import com.ergun.connectsphere.entity.UserEntity;
import com.ergun.connectsphere.repository.ChatGroupRepository;
import com.ergun.connectsphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatGroupService {

    private final ChatGroupRepository chatGroupRepository;
    private final UserRepository userRepository;

    public ChatGroupResponseDto createGroup(ChatGroupCreateRequestDto request) {

        UserEntity creator = userRepository.findById(request.getCreatorId())
                .orElseThrow(() -> new RuntimeException("Creator not found"));

        ChatGroupEntity group = ChatGroupEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .creator(creator)
                .members(new ArrayList<>())
                .build();

        group.getMembers().add(creator);

        ChatGroupEntity savedGroup = chatGroupRepository.save(group);

        return ChatGroupResponseDto.fromEntity(savedGroup);
    }

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
