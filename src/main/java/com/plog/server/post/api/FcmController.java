package com.plog.server.post.api;

import com.plog.server.post.dto.FcmSend;
import com.plog.server.post.service.FcmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sun.activation.registries.LogSupport.log;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;

    @PostMapping("/send")
    public ResponseEntity<String> sendFcmMessage(@Valid @RequestBody FcmSend fcmResponse) {
        log("푸시 메시시를 전송");
        try {
            fcmService.sendMessageTo(fcmResponse);
            return ResponseEntity.ok("FCM message sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send FCM message: " + e.getMessage());
        }
    }
}
