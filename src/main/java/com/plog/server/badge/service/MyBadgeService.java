package com.plog.server.badge.service;

import com.plog.server.badge.domain.Badge;
import com.plog.server.badge.domain.MyBadge;
import com.plog.server.badge.repository.MyBadgeRepository;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MyBadgeService {
    private final ProfileRepository profileRepository;
    private final MyBadgeRepository myBadgeRepository;
    @Transactional
    public String setSelectedBadge(UUID userUUID, int badgeId) {
        // 사용자 프로필 조회
        Profile profile = profileRepository.findByUserUserUUID(userUUID)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 없습니다: " + userUUID));
        // 해당 사용자가 소유한 배지인지 확인
        Optional<MyBadge> myBadgeOptional = myBadgeRepository.findByProfileAndBadgeBadgeId(profile, (long) badgeId);

        if (myBadgeOptional.isPresent()) {
            // 소유한 배지인 경우
            profile.setSelectedBadge(myBadgeOptional.get().getBadge());
            profileRepository.save(profile);
            return "프로필 배지가 성공적으로 설정되었습니다.";
        } else {
            // 배지를 소유하지 않은 경우
            return "해당 배지는 사용자가 소유하고 있지 않습니다.";
        }
    }

    //프로필 배지 조회
    public int getSelectedBadgeId(UUID userUUID) {
        // 사용자 프로필 조회
        Profile profile = profileRepository.findByUserUserUUID(userUUID)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 없습니다: " + userUUID));
        // 프로필에 설정된 배지가 있는지 확인
        Badge selectedBadge = profile.getBadge();
        if (selectedBadge == null) {
            throw new IllegalArgumentException("설정된 배지가 없습니다.");
        }
        // 배지 ID 반환
        return selectedBadge.getBadgeId().intValue();
    }
}
