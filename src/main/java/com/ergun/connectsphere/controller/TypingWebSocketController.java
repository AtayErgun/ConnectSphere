package com.ergun.connectsphere.controller;


import com.ergun.connectsphere.dto.TypingEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class TypingWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/typing")
    public void handleTyping(@Payload TypingEventDto typingEvent) {

        messagingTemplate.convertAndSend(
                "/topic/group/" + typingEvent.getGroupId() + "/typing",
                typingEvent
        );
    }
}