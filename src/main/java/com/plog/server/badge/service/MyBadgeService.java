package com.plog.server.badge.service;

import com.plog.server.badge.domain.MyBadge;
import com.plog.server.badge.repository.MyBadgeRepository;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MyBadgeService {
    private final ProfileRepository profileRepository;
    private final MyBadgeRepository myBadgeRepository;
    @Transactional
    public void setSelectedBadge(UUID userUUID, int badgeId) {
        // 사용자 프로필 조회
        Profile profile = profileRepository.findByUserUserUUID(userUUID)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 없습니다: " + userUUID));

        // 해당 사용자가 소유한 배지인지 확인
        MyBadge myBadge = myBadgeRepository.findByProfileAndBadgeBadgeId(profile, (long) badgeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 배지는 사용자가 소유하고 있지 않습니다."));

        // 프로필에 대표 배지 설정
        profile.setSelectedBadge(myBadge.getBadge());

        // 프로필 저장
        profileRepository.save(profile);
    }


}
