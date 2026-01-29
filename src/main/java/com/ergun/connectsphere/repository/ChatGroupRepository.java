package com.ergun.connectsphere.repository;

import com.ergun.connectsphere.entity.ChatGroupEntity;
import com.ergun.connectsphere.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ChatGroupRepository extends JpaRepository<ChatGroupEntity,Long> {
    List<ChatGroupEntity> findByMembersContaining(UserEntity user);

    @Query("""
    SELECT g FROM ChatGroupEntity g 
    JOIN g.members m1 
    JOIN g.members m2 
    WHERE g.isPrivate = true 
    AND m1.id = :user1Id 
    AND m2.id = :user2Id
""")
    Optional<ChatGroupEntity> findPrivateChatBetweenUsers(
            @Param("user1Id") Long user1Id,
            @Param("user2Id") Long user2Id
    );
}
