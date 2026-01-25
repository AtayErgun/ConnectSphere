package com.ergun.connectsphere.controller;


import com.ergun.connectsphere.MessageType;
import com.ergun.connectsphere.dto.MessageResponseDto;
import com.ergun.connectsphere.dto.MessageSendRequestDto;
import com.ergun.connectsphere.service.FileService;
import com.ergun.connectsphere.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@CrossOrigin
public class MessageController {

    private final MessageService messageService;
    private final FileService fileService;


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

    @PostMapping("/image")
    public MessageResponseDto sendImageMessage(
            @RequestParam MultipartFile file,
            @RequestParam Long senderId,
            @RequestParam Long groupId,
            @RequestParam(value = "content", required = false) String content
    ) {

        String fileName = fileService.saveFile(file);

        String imageUrl = "http://localhost:8080/uploads/" + fileName;

        return messageService.sendMessage(
                senderId,
                groupId,
                content,
                MessageType.IMAGE,
                imageUrl
        );
    }

    @PostMapping("/{id}/react")
    public void reactToMessage(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam String emoji
    ) {
        messageService.addReaction(id, userId, emoji);
    }

    // ✏️ EDIT
    @PutMapping("/{id}")
    public void editMessage(
            @PathVariable Long id,
            @RequestParam Long userId,
            @RequestParam String content
    ) {
        messageService.editMessage(id, userId, content);
    }

    // 🗑️ DELETE
    @DeleteMapping("/{id}")
    public void deleteMessage(
            @PathVariable Long id,
            @RequestParam Long userId
    ) {
        messageService.deleteMessage(id, userId);
    }
}