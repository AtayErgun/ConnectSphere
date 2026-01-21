package com.ergun.connectsphere.repository;

import com.ergun.connectsphere.entity.ChatGroupEntity;
import com.ergun.connectsphere.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity,Long> {
    List<MessageEntity> findByGroupOrderByTimestampAsc(ChatGroupEntity group);

}
