package com.ergun.connectsphere.repository;

import com.ergun.connectsphere.entity.MessageEntity;
import com.ergun.connectsphere.entity.MessageReadStatusEntity;
import com.ergun.connectsphere.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MessageReadStatusRepository extends JpaRepository<MessageReadStatusEntity,Long> {
    boolean existsByMessageAndUser(MessageEntity message, UserEntity user);

}
