package com.plog.server.plogging.repository;

import com.plog.server.plogging.domain.Activity;
import com.plog.server.plogging.domain.Location;
import com.plog.server.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity,Long> {
    List<Activity> findByProfile(Profile profile);
    Optional<Activity> findByProfileAndActivityId(Profile profile, Long activityId);
    List<Activity> findByRouteStatus(boolean b);
    List<Activity> findByProfileAndRouteStatus(Profile profile, Boolean routeStatus);
    Optional<Activity> findByActivityIdAndProfileUserUserUUID(Long activityId, UUID uuid);
}
