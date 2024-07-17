package com.plog.server.user.repository;

import com.plog.server.user.domain.UserTemp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTempRepository extends JpaRepository<UserTemp,Long> {
}
