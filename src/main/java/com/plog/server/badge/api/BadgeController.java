package com.plog.server.badge.api;

import com.plog.server.badge.dto.BadgeResponse;
import com.plog.server.badge.service.BadgeService;
import com.plog.server.global.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/badge")
@Slf4j
public class BadgeController {
    private final BadgeService badgeService;

    @GetMapping("/{badgeId}/conditions")
    public ApiResponse<BadgeResponse> getBadgeCondition(@PathVariable Long badgeId){
        BadgeResponse badgeResponse = badgeService.getBadgeCondition(badgeId);

        return new ApiResponse<>("배지 해금 조건 반환",badgeResponse);
    }

}
