package com.ergun.connectsphere.repository;

import com.ergun.connectsphere.entity.ChatGroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatGroupRepository extends JpaRepository<ChatGroupEntity,Long> {
}
