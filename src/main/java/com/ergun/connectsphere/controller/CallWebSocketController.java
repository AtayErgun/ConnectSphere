package com.ergun.connectsphere.controller;

import com.ergun.connectsphere.dto.CallEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class CallWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * WebRTC Signaling Endpoint
     * Client -> /app/call
     */
    @MessageMapping("/call")
    public void handleCallEvent(CallEventDto event) {
        // 📢 Bu print satırını ekle, sinyal sunucuya ulaşıyor mu görelim:
        System.out.println("☎️ Sinyal İletiliyor: " + event.getType() + " -> Hedef User: " + event.getToUserId());

        messagingTemplate.convertAndSend("/topic/users/" + event.getToUserId() + "/call", event);
    }
}