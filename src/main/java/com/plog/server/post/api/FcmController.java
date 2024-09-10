package com.plog.server.post.api;

import com.plog.server.global.ApiResponse;
import com.plog.server.post.dto.FcmSend;
import com.plog.server.post.dto.NoticeResponse;
import com.plog.server.post.service.FcmService;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.repository.ProfileRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.sun.activation.registries.LogSupport.log;

@Slf4j
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
    public ResponseEntity<ApiResponse> sendFcmMessage(@Valid @RequestBody FcmSend fcmResponse) {
        log("푸시 메시지를 전송");

        try {
            fcmService.sendMessageTo(fcmResponse);
            ApiResponse apiResponse = new ApiResponse("FCM 메시지가 성공적으로 전송되었습니다.", null);
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            log.error("FCM 메시지 전송 실패: {}", e.getMessage());
            ApiResponse apiResponse = new ApiResponse("FCM 메시지 전송 실패: " + e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
        }
    }
}
