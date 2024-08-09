package com.plog.server.plogging.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plog.server.plogging.dto.ActivityResponse;
import com.plog.server.plogging.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@org.springframework.transaction.annotation.Transactional
public class GeocodeService {

    private final String API_KEY = "AIzaSyCVaS_odaflKffoapNUYui3AE5rarygE9M";

    public ActivityResponse getAddress(double latitude, double longitude) {

        ActivityResponse activityResponse = new ActivityResponse();

        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&key=%s&language=ko", latitude, longitude, API_KEY);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println("Response Status Code: " + response.getStatusCode());

        String responseBody = response.getBody();
        System.out.println("Response Body: " + responseBody);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Map<String, Object> jsonResponse = objectMapper.readValue(response.getBody(), Map.class);
            List<Map<String, Object>> results = (List<Map<String, Object>>) jsonResponse.get("results");

            if (results != null && !results.isEmpty()) {
                Map<String, Object> result = results.get(0);
                String formattedAddress = (String) result.get("formatted_address");

                // 여기서 필요한 주소만 설정
                activityResponse.setPlace(extractRelevantPart(formattedAddress));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return activityResponse;
    }

    private String extractRelevantPart(String address) {
        String[] parts = address.split(" ");
        return String.join(" ", Arrays.copyOfRange(parts, 3, parts.length));
    }
}