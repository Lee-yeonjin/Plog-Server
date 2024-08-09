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
    public String getAddress(double latitude, double longitude) {
        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&key=%s&language=ko", latitude, longitude, API_KEY);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        String responseBody = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        String address = "";

        try {
            Map<String, Object> jsonResponse = objectMapper.readValue(responseBody, Map.class);
            List<Map<String, Object>> results = (List<Map<String, Object>>) jsonResponse.get("results");

            if (results != null && !results.isEmpty()) {
                Map<String, Object> result = results.get(0);
                address = (String) result.get("formatted_address");

                // 필요 시 주소에서 특정 부분만 추출
                address = extractRelevantPart(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }


    private String extractRelevantPart(String address) {
        String[] parts = address.split(" ");
        return String.join(" ", Arrays.copyOfRange(parts, 3, parts.length));
    }
}