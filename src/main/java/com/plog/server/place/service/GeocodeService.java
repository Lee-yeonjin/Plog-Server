package com.plog.server.place.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plog.server.place.domain.Place;
import com.plog.server.place.dto.GeocodeDTO;
import com.plog.server.place.repository.GeocodeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@org.springframework.transaction.annotation.Transactional
public class GeocodeService {

    private final GeocodeRepository geocodeRepository;
    private final String API_KEY = "AIzaSyCVaS_odaflKffoapNUYui3AE5rarygE9M";

    @Transactional
    public void createPlace(Place place) {
        geocodeRepository.save(place);
    }


    public GeocodeDTO getAddress(double latitude, double longitude) {
        GeocodeDTO responseDTO = new GeocodeDTO();

        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%f,%f&key=%s&language=ko", latitude, longitude, API_KEY);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println("Response Status Code: " + response.getStatusCode());

        String responseBody = response.getBody();
        System.out.println("Response Body: " + responseBody);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Map<String, Object> jsonResponse = objectMapper.readValue(responseBody, Map.class);
            List<Map<String, Object>> results = (List<Map<String, Object>>) jsonResponse.get("results");

            if (results != null && !results.isEmpty()) {
                Map<String, Object> result = results.get(0);
                String formattedAddress = (String) result.get("formatted_address");
                responseDTO.setPlace(formattedAddress);

                List<Map<String, Object>> addressComponents = (List<Map<String, Object>>) result.get("address_components");
                for (Map<String, Object> component : addressComponents) {
                    List<String> types = (List<String>) component.get("types");
                    if (types.contains("postal_code")) {
                        responseDTO.setZip((String) component.get("long_name"));
                    } else if (types.contains("locality")) {
                        responseDTO.setCity((String) component.get("long_name"));
                    } else if (types.contains("administrative_area_level_1")) {
                        responseDTO.setProvince((String) component.get("long_name"));
                    } else if (types.contains("administrative_area_level_2")) {
                        responseDTO.setRegion((String) component.get("long_name"));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseDTO;
    }
}