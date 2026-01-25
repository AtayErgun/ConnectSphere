package com.ergun.connectsphere.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatGroupSummaryDto {

    private Long groupId;
    private String groupName;
    private long unreadCount;
}