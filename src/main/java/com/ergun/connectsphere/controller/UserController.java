package com.ergun.connectsphere.controller;

import com.ergun.connectsphere.dto.UserRegisterRequestDto;
import com.ergun.connectsphere.dto.UserResponseDto;
import com.ergun.connectsphere.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;

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