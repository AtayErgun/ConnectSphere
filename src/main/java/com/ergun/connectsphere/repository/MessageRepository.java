package com.ergun.connectsphere.repository;

import com.ergun.connectsphere.entity.ChatGroupEntity;
import com.ergun.connectsphere.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity,Long> {
    List<MessageEntity> findByGroupOrderByTimestampAsc(ChatGroupEntity group);

    @Query("""
        SELECT COUNT(m)
        FROM MessageEntity m
        WHERE m.group.id = :groupId
        AND m.sender.id <> :userId
        AND m.id NOT IN (
            SELECT rs.message.id
            FROM MessageReadStatusEntity rs
            WHERE rs.user.id = :userId
        )
    """)
    long countUnreadMessages(
            @Param("groupId") Long groupId,
            @Param("userId") Long userId
    );
}
