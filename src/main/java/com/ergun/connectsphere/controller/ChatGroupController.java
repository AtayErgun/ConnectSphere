package com.ergun.connectsphere.controller;


import com.ergun.connectsphere.dto.ChatGroupCreateRequestDto;
import com.ergun.connectsphere.dto.ChatGroupResponseDto;
import com.ergun.connectsphere.dto.ChatGroupSummaryDto;
import com.ergun.connectsphere.service.ChatGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
@CrossOrigin
public class ChatGroupController {

    private final ChatGroupService chatGroupService;

    @PostMapping
    public ResponseEntity<ChatGroupResponseDto> createGroup(
            @RequestBody ChatGroupCreateRequestDto request
    ) {
        return ResponseEntity.ok(chatGroupService.createGroup(request));
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<ChatGroupResponseDto> addMemberToGroup(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        return ResponseEntity.ok(
                chatGroupService.addMemberToGroup(id, userId)
        );
    }

    @GetMapping
    public ResponseEntity<List<ChatGroupResponseDto>> getAllGroups() {
        return ResponseEntity.ok(chatGroupService.getAllGroups());
    }

    @GetMapping("/summary")
    public List<ChatGroupSummaryDto> getGroupSummaries(
            @RequestParam Long userId
    ) {
        return chatGroupService.getGroupSummaries(userId);
    }
}