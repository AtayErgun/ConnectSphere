package com.ergun.connectsphere.controller;


import com.ergun.connectsphere.dto.MessageResponseDto;
import com.ergun.connectsphere.dto.MessageSendRequestDto;
import com.ergun.connectsphere.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@CrossOrigin
public class MessageController {

    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponseDto> sendMessage(
            @RequestBody MessageSendRequestDto request
    ) {
        return ResponseEntity.ok(
                messageService.sendMessage(
                        request.getSenderId(),
                        request.getGroupId(),
                        request.getContent(),
                        request.getMessageType(),
                        request.getAttachmentUrl()
                )
        );
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<MessageResponseDto>> getMessagesByGroup(
            @PathVariable Long groupId
    ) {
        return ResponseEntity.ok(
                messageService.getMessagesByGroup(groupId)
        );
    }
    @PutMapping("/{messageId}/read")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long messageId,
            @RequestParam Long userId
    ) {
        // Mesajı okundu olarak işaretle
        messageService.markMessageAsRead(messageId, userId);
        return ResponseEntity.ok().build();
    }

}