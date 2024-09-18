package com.plog.server.badge.api;

import com.plog.server.badge.dto.BadgeResponse;
import com.plog.server.badge.service.BadgeService;
import com.plog.server.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/badge")
@Slf4j
public class BadgeController {
    private final BadgeService badgeService;

    @GetMapping("/{badgeId}/conditions")
    public ApiResponse<BadgeResponse> getBadgeCondition(@PathVariable int badgeId){
        BadgeResponse badgeResponse = badgeService.getBadgeCondition(badgeId);

        return new ApiResponse<>("배지 해금 조건 반환",badgeResponse);
    }

    // 배지 해금 조건 체크
    @PostMapping("/{uuid}/{badgeId}/check")
    public ApiResponse<String> checkBadgeConditions(@PathVariable UUID uuid, @PathVariable int badgeId) {
        String result = badgeService.checkBadgeConditions(uuid, badgeId);
        return new ApiResponse<>("배지 체크", result);
    }

    //배지 구매
    @PostMapping("/{uuid}/{badgeId}/purchase")
    public ApiResponse<String> purchaseBadge(@PathVariable UUID uuid, @PathVariable int badgeId) {
        String result = badgeService.purchaseBadge(uuid, badgeId);
        return new ApiResponse<>("배지 구매", result);
    }
}
