package com.ergun.connectsphere.config;

import com.ergun.connectsphere.entity.ChatGroupEntity;
import com.ergun.connectsphere.entity.UserEntity;
import com.ergun.connectsphere.repository.ChatGroupRepository;
import com.ergun.connectsphere.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ChatGroupRepository chatGroupRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Eğer kullanıcı yoksa oluştur
        if (userRepository.count() == 0) {
            UserEntity testUser = UserEntity.builder()
                    .username("ergun")
                    .email("test@test.com")
                    .password(passwordEncoder.encode("123"))
                    .build();
            userRepository.save(testUser);

            // Eğer grup yoksa oluştur
            if (chatGroupRepository.count() == 0) {
                ChatGroupEntity generalGroup = ChatGroupEntity.builder()
                        .name("Genel Sohbet")
                        .description("ConnectSphere İlk Grubu")
                        .creator(testUser)
                        .members(new ArrayList<>())
                        .build();

                generalGroup.getMembers().add(testUser);
                chatGroupRepository.save(generalGroup);

                System.out.println(">>> Test verileri başarıyla oluşturuldu.");
            }
        }
    }
}