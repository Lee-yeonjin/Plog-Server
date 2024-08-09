package com.plog.server.plogging.repository;

import com.plog.server.plogging.domain.Activity;
import com.plog.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity,Long> {
    List<Activity> findByUser(User user);
    Optional<Activity> findByUserAndActivityId(User user, Long activityId);
    List<Activity> findByRouteStatus(boolean b);

    List<Activity> findByUserAndRouteStatus(User user, Boolean routeStatus);
}
