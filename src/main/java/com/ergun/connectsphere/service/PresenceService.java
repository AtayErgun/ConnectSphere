package com.ergun.connectsphere.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class PresenceService {

    private final Set<Long> onlineUsers = ConcurrentHashMap.newKeySet();
    private final SimpMessagingTemplate messagingTemplate;

    public PresenceService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void userOnline(Long userId) {
        onlineUsers.add(userId);

        messagingTemplate.convertAndSend(
                "/topic/presence",
                Map.of(
                        "type", "ONLINE",
                        "userId", userId
                )
        );
    }

    public void userOffline(Long userId) {
        onlineUsers.remove(userId);

        messagingTemplate.convertAndSend(
                "/topic/presence",
                Map.of(
                        "type", "OFFLINE",
                        "userId", userId
                )
        );
    }

    public Set<Long> getOnlineUsers() {
        return onlineUsers;
    }
}
