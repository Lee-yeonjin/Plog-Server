package com.plog.server.plogging.repository;

import com.plog.server.plogging.domain.ActivityPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityPhotoRepository extends JpaRepository<ActivityPhoto,Long> {
    ActivityPhoto findByActivity_ActivityId(Long activityId);
}
