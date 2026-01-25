package com.ergun.connectsphere.service;


import com.ergun.connectsphere.dto.AuthLoginRequestDto;
import com.ergun.connectsphere.dto.AuthRegisterRequestDto;
import com.ergun.connectsphere.dto.AuthResponseDto;
import com.ergun.connectsphere.entity.UserEntity;
import com.ergun.connectsphere.repository.ChatGroupRepository;
import com.ergun.connectsphere.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ChatGroupRepository chatGroupRepository; // Yeni eklendi
    private final PasswordEncoder passwordEncoder;

    @Transactional // Hem kullanıcıyı kaydedip hem gruba eklediğimiz için önemli
    public AuthResponseDto register(AuthRegisterRequestDto request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already in use");
        }

        UserEntity user = UserEntity.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        UserEntity savedUser = userRepository.save(user);

        // --- OTOMATİK GRUBA EKLEME KISMI ---
        // ID'si 1L olan "Genel Sohbet" grubunu bul ve kullanıcıyı içine at
        chatGroupRepository.findById(1L).ifPresent(group -> {
            group.getMembers().add(savedUser);
            chatGroupRepository.save(group);
        });
        // -----------------------------------

        return AuthResponseDto.fromEntity(savedUser);
    }

    public AuthResponseDto login(AuthLoginRequestDto request) {

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return AuthResponseDto.fromEntity(user);
    }
}