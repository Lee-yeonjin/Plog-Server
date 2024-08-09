package com.plog.server.plogging.api;

import com.plog.server.global.ApiResponse;
import com.plog.server.plogging.domain.Location;
import com.plog.server.plogging.dto.LocationMessage;
import com.plog.server.plogging.repository.LocationRepository;
import com.plog.server.plogging.service.LocationService;
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
    private final UserService userService;

    @MessageMapping("/location")
    @SendTo("/topic/locations")
    public ApiResponse handleLocation(LocationMessage message) {

        UUID uuid = message.getUuid();

        User user = userService.getUserByUuid(uuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found with UUID: " + uuid));

        Location location = Location.builder()
                .user(user)
                .latitude(message.getLatitude())
                .longitude(message.getLongitude())
                .build();

        locationRepository.save(location);

        log.info("실시간 위치 정보 받기: {}", message);

        return new ApiResponse("위치 받아오기",location);
    }

    @GetMapping("/locations/{uuid}")
    public List<Location> getUserLocations(@PathVariable UUID uuid) {
        return locationService.getUserLocations(uuid);
    }

}
