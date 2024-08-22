package com.plog.server.post.repository;

import com.plog.server.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByProfile_UserUserUUID(UUID userUUID);
}
