package com.plog.server.badge.repository;

import com.plog.server.badge.domain.MyBadge;
import com.plog.server.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MyBadgeRepository extends JpaRepository <MyBadge,Long> {
    Optional<MyBadge> findByProfileAndBadgeBadgeId(Profile profile, Long badgeId);
}
