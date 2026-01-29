package com.ergun.connectsphere.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "chat_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatGroupEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    // 🟢 YENİ: Bu alan true ise grup 2 kişilik bir DM'dir.
    @Builder.Default
    @Column(nullable = false)
    private boolean isPrivate = false;

    // Grubu oluşturan kullanıcı
    // DM'lerde sohbeti ilk başlatan kişi creator olarak atanacak.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private UserEntity creator;

    // Grup üyeleri
    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "group_members",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> members = new ArrayList<>();
}
