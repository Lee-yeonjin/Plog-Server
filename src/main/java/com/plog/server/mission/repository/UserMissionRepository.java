package com.plog.server.mission.repository;

import com.plog.server.mission.domain.Mission;
import com.plog.server.mission.domain.UserMission;
import com.plog.server.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserMissionRepository extends JpaRepository<UserMission, Long> {
    List<UserMission> findByProfile(Profile profile);
    Optional<UserMission> findByProfileAndMission(Profile profile, Mission mission);
}