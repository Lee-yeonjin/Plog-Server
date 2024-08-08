package com.plog.server.post.repository;

import com.plog.server.post.domain.Join;
import com.plog.server.post.domain.Post;
import com.plog.server.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JoinRepository extends JpaRepository<Join,Long> {
    boolean existsByUserAndPost(User user, Post post);
    Optional<Join> findByUserAndPost(User user, Post post);
    @Query("SELECT j FROM Join j WHERE j.user = :user")
    List<Join> findByUser(@Param("user") User user);
}
