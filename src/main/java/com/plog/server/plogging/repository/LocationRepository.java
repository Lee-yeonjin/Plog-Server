package com.plog.server.plogging.repository;

import com.plog.server.plogging.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByUserUserUUID(UUID uuid);

    List<Location> findByUserUserUUIDAndActivityIsNull(UUID uuid);
}
