package com.plog.server.post.repository;

import com.plog.server.post.domain.Like;
import com.plog.server.post.domain.Post;
import com.plog.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByUserAndPost(User user, Post post);
    Optional<Like> findByUserAndPost(User user, Post post);
    List<Like> findByUser(User user);
}