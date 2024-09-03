package com.plog.server.post.api;

import com.plog.server.post.dto.FcmResponse;
import com.plog.server.post.service.FcmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/send")
    public ResponseEntity<String> sendFcmMessage(@Valid @RequestBody FcmResponse fcmResponse) {
        try {
            fcmService.sendMessageTo(fcmResponse.getTargetToken(), fcmResponse.getTitle(), fcmResponse.getBody());
            return ResponseEntity.ok("FCM message sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send FCM message: " + e.getMessage());
        }
    }
}
