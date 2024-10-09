package com.plog.server.profile.api;

import com.plog.server.global.ApiResponse;
import com.plog.server.post.dto.NoticeRequest;
import com.plog.server.post.dto.NoticeResponse;
import com.plog.server.post.service.FcmService;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.dto.ActiveProfileResponse;
import com.plog.server.profile.dto.ProfileResponse;
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

    @PostMapping("/{uuid}/membership")
    public ResponseEntity<ApiResponse<Boolean>> membershipTrue(@PathVariable UUID uuid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        profile.setUserMembership(true);
        profileRepository.save(profile);

        ApiResponse<Boolean> apiResponse = new ApiResponse<>("멤버십 가입 성공", profile.isUserMembership());
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/{uuid}/membershipcancel")
    public ResponseEntity<ApiResponse<Boolean>> membershipcancel(@PathVariable UUID uuid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        profile.setUserMembership(false);
        profileRepository.save(profile);

        ApiResponse<Boolean> apiResponse = new ApiResponse<>("멤버십 해지 성공", profile.isUserMembership());
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ActiveProfileResponse>>> getActivePloggingDetails() {
        List<ActiveProfileResponse> activeProfiles = profileService.getActivePloggingDetails();
        ApiResponse<List<ActiveProfileResponse>> response = new ApiResponse<>("플로깅중인 사용자 조회 성공", activeProfiles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // FCM 관련
    @GetMapping("/{uuid}/MyPage")
    public ResponseEntity<ApiResponse<NoticeRequest>> getfcmMypage(@PathVariable UUID uuid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        // 프로필 ID로 FCM 정보를 가져옵니다.
        NoticeRequest noticeRequest = fcmService.getNoticeRequestByProfileId(profile);

        ApiResponse<NoticeRequest> apiResponse = new ApiResponse<>("마이페이지 조회 성공", noticeRequest);
        return ResponseEntity.ok(apiResponse);
    }

    // FCM 제외 정보들 마이페이지 창 접속할 때
    @GetMapping("/{uuid}/MyPageInformation")
    public ResponseEntity<ApiResponse<ProfileResponse>> getMypage(@PathVariable UUID uuid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        // ProfileService를 통해 ProfileResponse 생성
        ProfileResponse profileResponse = profileService.getMypage(profile);

        ApiResponse<ProfileResponse> apiResponse = new ApiResponse<>("마이페이지 조회 성공", profileResponse);
        return ResponseEntity.ok(apiResponse);
    }
}

