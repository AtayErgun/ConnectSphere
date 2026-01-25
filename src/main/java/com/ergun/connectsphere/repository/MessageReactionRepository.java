package com.ergun.connectsphere.repository;

import com.ergun.connectsphere.entity.MessageReactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageReactionRepository
        extends JpaRepository<MessageReactionEntity, Long> {

    Optional<MessageReactionEntity> findByMessageIdAndUserIdAndEmojiCode(
            Long messageId,
            Long userId,
            String emojiCode
    );
}
