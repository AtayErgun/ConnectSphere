package com.ergun.connectsphere.dto;



import com.ergun.connectsphere.entity.ChatGroupEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatGroupResponseDto {

    private Long id;
    private String name;
    private String description;
    private Long creatorId;
    private List<Long> memberIds;

    public static ChatGroupResponseDto fromEntity(ChatGroupEntity group) {
        return ChatGroupResponseDto.builder()
                .id(group.getId())
                .name(group.getName())
                .description(group.getDescription())
                .creatorId(group.getCreator().getId())
                .memberIds(
                        group.getMembers()
                                .stream()
                                .map(member -> member.getId())
                                .toList()
                )
                .build();
    }
}
