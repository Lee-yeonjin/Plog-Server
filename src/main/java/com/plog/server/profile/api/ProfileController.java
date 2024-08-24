package com.plog.server.profile.api;

import com.plog.server.global.ApiResponse;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.dto.ActiveProfileResponse;
import com.plog.server.profile.service.ProfileService;
import com.plog.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
@Slf4j
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ActiveProfileResponse>>> getActivePloggingDetails() {
        List<ActiveProfileResponse> activeProfiles = profileService.getActivePloggingDetails();
        ApiResponse<List<ActiveProfileResponse>> response = new ApiResponse<>("플로깅중인 사용자 조회 성공", activeProfiles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

