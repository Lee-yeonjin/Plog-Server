package com.plog.server.rank.repository;

import com.plog.server.rank.domain.Rank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RankRepository extends JpaRepository<Rank, Long> {
}
