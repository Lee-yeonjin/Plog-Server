package com.plog.server.user.repository;

import com.plog.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserId(Long userId);
    User findByUserAccount(String userAccount);
    Optional<User> findByUserUUID(UUID useruuid);
}
