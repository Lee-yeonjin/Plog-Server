package com.plog.server.mission.repository;

import com.plog.server.mission.domain.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepository extends JpaRepository<Mission, Long> {
    // 필요한 추가 쿼리 메서드가 있다면 여기에 추가
}
