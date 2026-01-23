package com.ergun.connectsphere.controller;

import com.ergun.connectsphere.dto.AuthLoginRequestDto;
import com.ergun.connectsphere.dto.AuthRegisterRequestDto;
import com.ergun.connectsphere.dto.AuthResponseDto;
import com.ergun.connectsphere.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(
            @RequestBody AuthRegisterRequestDto request
    ) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(
            @RequestBody AuthLoginRequestDto request
    ) {
        return ResponseEntity.ok(authService.login(request));
    }
}