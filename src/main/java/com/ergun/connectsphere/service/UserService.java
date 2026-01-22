package com.ergun.connectsphere.service;

import com.ergun.connectsphere.dto.UserRegisterRequestDto;
import com.ergun.connectsphere.dto.UserResponseDto;
import com.ergun.connectsphere.entity.UserEntity;
import com.ergun.connectsphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDto register(UserRegisterRequestDto request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        UserEntity savedUser = userRepository.save(user);

        return UserResponseDto.fromEntity(savedUser);
    }

    public List<UserResponseDto> searchUsers(String keyword) {
        return userRepository.findAll()
                .stream()
                .filter(user ->
                        user.getUsername().toLowerCase().contains(keyword.toLowerCase()) ||
                                user.getEmail().toLowerCase().contains(keyword.toLowerCase())
                )
                .map(UserResponseDto::fromEntity)
                .toList();
    }
}