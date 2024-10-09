package com.plog.server.post.repository;

import com.plog.server.post.domain.Post;
import com.plog.server.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByProfile(Profile profile);
    @Query("SELECT p FROM Join j JOIN j.post p WHERE j.profile = :profile")
    List<Post> findJoinedPostsByProfile(Profile profile);
}
