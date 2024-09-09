package com.plog.server.post.api;

import com.plog.server.global.ApiResponse;
import com.plog.server.post.dto.FcmSend;
import com.plog.server.post.dto.NoticeResponse;
import com.plog.server.post.service.FcmService;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.repository.ProfileRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.sun.activation.registries.LogSupport.log;

@RestController
@RequestMapping("/api/fcm")
@RequiredArgsConstructor
public class FcmController {

    private final FcmService fcmService;
    private final ProfileRepository profileRepository;

    // 알림설정여부
    @PostMapping("/{uuid}/notice")
    public ResponseEntity<ApiResponse> noticeFcm(@PathVariable UUID uuid, @RequestBody NoticeResponse noticeResponse) {

        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        fcmService.saveOrUpdateFcm(profile, noticeResponse);

        ApiResponse apiResponse = new ApiResponse("알림 설정 성공", null);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendFcmMessage(@Valid @RequestBody FcmSend fcmResponse) {
        log("푸시 메시지를 전송");
        try {
            fcmService.sendMessageTo(fcmResponse);
            return ResponseEntity.ok("FCM message sent successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send FCM message: " + e.getMessage());
        }
    }

}
