package com.plog.server.plogging.api;

import com.plog.server.global.ApiResponse;
import com.plog.server.plogging.domain.Location;
import com.plog.server.plogging.dto.LocationMessage;
import com.plog.server.plogging.repository.LocationRepository;
import com.plog.server.plogging.service.LocationService;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.repository.ProfileRepository;
import com.plog.server.user.domain.User;
import com.plog.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LocationController {

    private final LocationRepository locationRepository;
    private final LocationService locationService;
    private final ProfileRepository profileRepository;

    @MessageMapping("/location")
    @SendTo("/topic/locations")
    public ApiResponse handleLocation(LocationMessage message) {

        UUID uuid = message.getUuid();

        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 없습니다" + uuid));

        Location location = Location.builder()
                .profile(profile)
                .latitude(message.getLatitude())
                .longitude(message.getLongitude())
                .build();

        locationRepository.save(location);

        log.info("실시간 위치 정보 받기: {}", message);

        return new ApiResponse("위치 받아오기",location);
    }

}
