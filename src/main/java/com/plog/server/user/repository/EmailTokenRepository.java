package com.plog.server.user.repository;

import com.plog.server.user.domain.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTokenRepository extends JpaRepository<EmailToken,Long> {
}
