package com.plog.server.place.repository;

import com.plog.server.place.domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeocodeRepository extends JpaRepository<Place, Long>  {
}
