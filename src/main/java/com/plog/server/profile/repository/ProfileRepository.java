package com.plog.server.profile.repository;

import com.plog.server.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile,Long> {
    Optional<Profile> findByUserUserUUID(UUID userUUID);
    List<Profile> findByPloggingStatus(boolean PloggingStatus);
}

