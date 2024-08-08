package com.plog.server.plogging.repository;

import com.plog.server.plogging.domain.Activity;
import com.plog.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity,Long> {
    List<Activity> findByUser(User user);
}
