package com.plog.server.badge.api;

import com.google.protobuf.Api;
import com.plog.server.badge.service.MyBadgeService;
import com.plog.server.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mybadge")
@Slf4j
public class MyBadgeController {
    private final MyBadgeService myBadgeService;

    //프로필 배지 설정
    @PatchMapping("/{userUUID}/{badgeId}")
    public ResponseEntity<ApiResponse<Void>> updateSelectedBadge(
            @PathVariable UUID userUUID,
            @PathVariable int badgeId) {
        String message = myBadgeService.setSelectedBadge(userUUID, badgeId);

        ApiResponse<Void> response = new ApiResponse<>(message);

        return ResponseEntity.ok(response);
    }

    //프로필 배지 조회
    @GetMapping("/{uuid}")
    public ApiResponse<Integer> getMyBadge(@PathVariable UUID uuid){
        Integer badgeId = myBadgeService.getSelectedBadgeId(uuid);
        return new ApiResponse<>("프로필 배지", badgeId);
    }
}
