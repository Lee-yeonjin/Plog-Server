package com.plog.server.trash.repository;

import com.plog.server.plogging.domain.Activity;
import com.plog.server.trash.domain.Trash;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrashRepository extends JpaRepository<Trash, Long> {
    List<Trash> findByActivity(Activity activity);
}