package com.ergun.connectsphere.controller;

import com.ergun.connectsphere.dto.UserRegisterRequestDto;
import com.ergun.connectsphere.dto.UserResponseDto;
import com.ergun.connectsphere.service.PresenceService;
import com.ergun.connectsphere.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final PresenceService presenceService;

    @GetMapping("/online")
    public Set<Long> getOnlineUsers() {
        return presenceService.getOnlineUsers();
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(
            @RequestBody UserRegisterRequestDto request
    ) {
        return ResponseEntity.ok(userService.register(request));
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> searchUsers(
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(userService.searchUsers(keyword));
    }
}