package com.plog.server.badge.repository;

import com.plog.server.badge.domain.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge,Long> {
    List<Badge> findByBadgeIdIn(List<Long> ownedBadgeIds);
}
