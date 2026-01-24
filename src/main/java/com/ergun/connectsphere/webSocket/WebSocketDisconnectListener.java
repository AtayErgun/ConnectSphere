package com.ergun.connectsphere.webSocket;

import com.ergun.connectsphere.service.PresenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
public class WebSocketDisconnectListener {

    private final PresenceService presenceService;

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        Long userId = (Long) accessor.getSessionAttributes().get("userId");

        if (userId != null) {
            presenceService.userOffline(userId);
        }
    }
}
