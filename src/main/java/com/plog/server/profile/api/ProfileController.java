package com.plog.server.profile.api;

import com.plog.server.global.ApiResponse;
import com.plog.server.post.dto.NoticeRequest;
import com.plog.server.post.dto.NoticeResponse;
import com.plog.server.post.service.FcmService;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.dto.ActiveProfileResponse;
import com.plog.server.profile.repository.ProfileRepository;
import com.plog.server.profile.service.ProfileService;
import com.plog.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
@Slf4j
public class ProfileController {
    private final FcmService fcmService;
    private final ProfileService profileService;
    private final ProfileRepository profileRepository;

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ActiveProfileResponse>>> getActivePloggingDetails() {
        List<ActiveProfileResponse> activeProfiles = profileService.getActivePloggingDetails();
        ApiResponse<List<ActiveProfileResponse>> response = new ApiResponse<>("플로깅중인 사용자 조회 성공", activeProfiles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{uuid}/MyPage")
    public ResponseEntity<ApiResponse<NoticeRequest>> getMypage(@PathVariable UUID uuid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        // 프로필 ID로 FCM 정보를 가져옵니다.
        NoticeRequest noticeRequest = fcmService.getNoticeRequestByProfileId(profile);

        ApiResponse<NoticeRequest> apiResponse = new ApiResponse<>("마이페이지 조회 성공", noticeRequest);
        return ResponseEntity.ok(apiResponse);
    }
}

