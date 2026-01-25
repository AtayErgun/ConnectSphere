package com.ergun.connectsphere.entity;

import com.ergun.connectsphere.MessageType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(nullable = false)
    private boolean isDeleted = false;

    // Mesajı atan kullanıcı
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity sender;

    // Mesajın ait olduğu grup
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false)
    private ChatGroupEntity group;

    @Builder.Default // Builder ile oluştururken varsayılan değeri korumasını sağlar
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MessageReactionEntity> reactions = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MessageType messageType;

    private String attachmentUrl;
}
