package com.plog.server.post.repository;

import com.plog.server.post.domain.Fcm;
import com.plog.server.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FcmRepository extends JpaRepository<Fcm, Long> {
    Optional<Fcm> findByProfile(Profile profile);
    List<Fcm> findByNotificationEnabledTrue();
}
