package com.plog.server.post.repository;

import com.plog.server.post.domain.Join;
import com.plog.server.post.domain.Post;
import com.plog.server.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JoinRepository extends JpaRepository<Join,Long> {
    boolean existsByProfileAndPost(Profile profile, Post post);
    Optional<Join> findByProfileAndPost(Profile profile, Post post);
    @Query("SELECT j FROM Join j WHERE j.profile = :profile")
    List<Join> findByProfile(@Param("profile") Profile profile);
}
