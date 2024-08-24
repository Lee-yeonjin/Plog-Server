package com.plog.server.badge.api;

import com.plog.server.badge.service.MyBadgeService;
import com.plog.server.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mybadge")
@Slf4j
public class MyBadgeController {
    private final MyBadgeService myBadgeService;

    @PatchMapping("/{userUUID}/{badgeId}")
    public ResponseEntity<ApiResponse<Void>> updateSelectedBadge(
            @PathVariable UUID userUUID,
            @PathVariable Long badgeId) {
        myBadgeService.setSelectedBadge(userUUID, badgeId);
        // 응답 메시지 생성
        ApiResponse<Void> response = new ApiResponse<>("대표 배지가 설정되었습니다.");

        return ResponseEntity.ok(response);
    }
}
