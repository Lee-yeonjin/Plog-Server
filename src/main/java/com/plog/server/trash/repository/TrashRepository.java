package com.plog.server.trash.repository;

import com.plog.server.trash.domain.Trash;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrashRepository extends JpaRepository<Trash, Long> {
}