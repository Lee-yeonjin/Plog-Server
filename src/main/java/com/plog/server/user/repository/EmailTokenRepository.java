package com.plog.server.user.repository;

import com.plog.server.user.domain.EmailToken;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailTokenRepository extends JpaRepository<EmailToken,Long> {
    @EntityGraph(attributePaths = "userTemp")
    Optional<EmailToken> findByEmailUuid(String uuid);
}
