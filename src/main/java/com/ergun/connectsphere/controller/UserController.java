package com.ergun.connectsphere.controller;

import com.ergun.connectsphere.dto.AuthResponseDto;
import com.ergun.connectsphere.dto.UserRegisterRequestDto;
import com.ergun.connectsphere.dto.UserResponseDto;
import com.ergun.connectsphere.dto.UserUpdateRequestDto;
import com.ergun.connectsphere.entity.UserEntity;
import com.ergun.connectsphere.repository.UserRepository;
import com.ergun.connectsphere.service.FileService;
import com.ergun.connectsphere.service.PresenceService;
import com.ergun.connectsphere.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserRepository userRepository;
    private final FileService fileService;
    private final SimpMessagingTemplate messagingTemplate;
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

    @PutMapping("/{id}")
    public  ResponseEntity<AuthResponseDto> updateProfile(
            @PathVariable Long id,
            @RequestBody UserUpdateRequestDto request
    ) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        UserEntity updated = userRepository.save(user);

        AuthResponseDto responseDto = AuthResponseDto.fromEntity(updated);
        publishProfileUpdate(responseDto);

        return ResponseEntity.ok(responseDto);
    }

    // 🔹 Upload avatar
    @PostMapping("/{id}/avatar")
    public  ResponseEntity<AuthResponseDto> uploadAvatar(
            @PathVariable Long id,
            @RequestParam MultipartFile file
    ) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String fileName = fileService.saveFile(file, "avatars");
        String avatarUrl = fileService.saveFile(file, "avatars");

        user.setAvatarUrl(avatarUrl);
        UserEntity updated = userRepository.save(user);

        AuthResponseDto responseDto = AuthResponseDto.fromEntity(updated);
        publishProfileUpdate(responseDto);

        return ResponseEntity.ok(responseDto);
    }

    // 🔥 WebSocket publish logic
    private void publishProfileUpdate(AuthResponseDto userDto) {
        // Kendi kanalına gönder
        messagingTemplate.convertAndSend(
                "/topic/users/" + userDto.getId() + "/update",
                userDto
        );

        // Online herkese duyur (Sadece ID ve mesaj göndermek daha güvenlidir)
        presenceService.getOnlineUserIds().forEach(onlineUserId ->
                messagingTemplate.convertAndSend(
                        "/topic/users/" + onlineUserId + "/presence",
                        userDto.getUsername() + " profilini güncelledi"
                )
        );
    }
}