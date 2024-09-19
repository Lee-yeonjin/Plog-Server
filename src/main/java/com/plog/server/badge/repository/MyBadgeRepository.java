package com.plog.server.badge.repository;

import com.plog.server.badge.domain.Badge;
import com.plog.server.badge.domain.MyBadge;
import com.plog.server.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface MyBadgeRepository extends JpaRepository <MyBadge,Long> {
    Optional<MyBadge> findByProfileAndBadgeBadgeId(Profile profile, Long badgeId);
//    @Query("SELECT mb FROM MyBadge mb WHERE mb.profile = :profile AND mb.myBadgeStatus = true")
//    List<MyBadge> findMainBadgesByProfile(@Param("profile") Profile profile);
    List<MyBadge> findByProfile(Profile profile);
    Optional<MyBadge> findByProfileAndBadge(Profile topProfile, Badge firstPlaceBadge);
    List<MyBadge> findBadgeIdsByProfile(Profile profile);
}