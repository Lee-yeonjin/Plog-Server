package com.plog.server.trash.repository;

import com.plog.server.plogging.domain.Activity;
import com.plog.server.trash.domain.Trash;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrashRepository extends JpaRepository<Trash, Long> {
    Optional<Trash> findByActivity(Activity activity);
}