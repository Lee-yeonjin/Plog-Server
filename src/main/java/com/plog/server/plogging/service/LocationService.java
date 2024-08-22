package com.plog.server.plogging.service;

import com.plog.server.plogging.domain.Location;
import com.plog.server.plogging.repository.LocationRepository;
import com.plog.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class LocationService {

    private final LocationRepository locationRepository;

}
