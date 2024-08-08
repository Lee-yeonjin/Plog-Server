package com.plog.server.place.api;

import com.plog.server.place.domain.Place;
import com.plog.server.place.dto.GeocodeDTO;
import com.plog.server.place.service.GeocodeService;
import com.plog.server.user.domain.User;
import com.plog.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/place")

public class GeocodeController {
    private final GeocodeService geocodeService;
    private final UserService userService;

    @PostMapping("/{uuid}/geocode")
    public Map<String, Object> getAddress(@PathVariable UUID uuid, @RequestBody GeocodeDTO geocodeDTO) {

        User user = userService.getUserByUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Map<String, Object> response = new LinkedHashMap<>();

        GeocodeDTO resultDTO1 = geocodeService.getAddress(geocodeDTO.getLatitude1(), geocodeDTO.getLongitude1());
        String place1 = resultDTO1.getPlace() != null ? resultDTO1.getPlace() : "";
        response.put("place1", place1);

        GeocodeDTO resultDTO2 = geocodeService.getAddress(geocodeDTO.getLatitude2(), geocodeDTO.getLongitude2());
        String place2 = resultDTO2.getPlace() != null ? resultDTO2.getPlace() : "";
        response.put("place2", place2);

        Place place = Place.builder()
                .place1(place1)
                .place2(place2)
                .latitude1(geocodeDTO.getLatitude1())
                .latitude2(geocodeDTO.getLatitude2())
                .longitude1(geocodeDTO.getLongitude1())
                .longitude2(geocodeDTO.getLongitude2())
                .build();

        geocodeService.createPlace(place);

        return response;
    }
}

