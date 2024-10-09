package com.plog.server.post.repository;

import com.plog.server.post.domain.Like;
import com.plog.server.post.domain.Post;
import com.plog.server.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByProfileAndPost(Profile profile, Post post);
    Optional<Like> findByProfileAndPost(Profile profile, Post post);
    List<Like> findByProfile(Profile profile);
}