package com.ergun.connectsphere.repository;

import com.ergun.connectsphere.entity.ChatGroupEntity;
import com.ergun.connectsphere.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ChatGroupRepository extends JpaRepository<ChatGroupEntity,Long> {
    List<ChatGroupEntity> findByMembersContaining(UserEntity user);
}
