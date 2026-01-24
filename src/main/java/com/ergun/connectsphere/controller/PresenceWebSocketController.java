package com.ergun.connectsphere.controller;

import com.ergun.connectsphere.service.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class PresenceWebSocketController {

    private final PresenceService presenceService;

    @MessageMapping("/online")
    public void setUserOnline(@Payload Long userId) {
        presenceService.userOnline(userId);
    }
}
