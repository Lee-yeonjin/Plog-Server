package com.plog.server.badge.service;

import com.plog.server.badge.domain.Badge;
import com.plog.server.badge.dto.BadgeResponse;
import com.plog.server.badge.repository.BadgeRepository;
import com.plog.server.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BadgeService {
    private final BadgeRepository badgeRepository;

    public BadgeResponse getBadgeCondition(Long badgeId) {
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 배지를 찾을 수 없습니다."));

        return new BadgeResponse(badge.getBadgeId().intValue(), badge.getBadgeGoal(), badge.getCost());
    }
}
