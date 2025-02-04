package com.plog.server.profile.service;

import com.plog.server.post.domain.Fcm;
import com.plog.server.post.dto.NoticeRequest;
import com.plog.server.post.repository.FcmRepository;
import com.plog.server.post.service.FcmService;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.dto.ActiveProfileResponse;
import com.plog.server.profile.dto.ProfileResponse;
import com.plog.server.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ProfileService {
    private final FcmService fcmService;
    private final ProfileRepository profileRepository;
    private final FcmRepository fcmRepository;

    public List<ActiveProfileResponse> getActivePloggingDetails() {
        List<Profile> activeProfiles = profileRepository.findByPloggingStatus(true);

        return activeProfiles.stream()
                .map(profile -> new ActiveProfileResponse(profile.getUserNickname(), profile.getBadge().getBadgeId()))
                .collect(Collectors.toList());
    }
    public Optional<Profile> getProfileByUserUUID(UUID userUUID) {
        return profileRepository.findByUserUserUUID(userUUID);
    }

    public ProfileResponse getMypage(Profile profile) {
        return new ProfileResponse(
                profile.getUserNickname(),
                profile.getTotalCoin(),
                profile.getBadge() != null ? profile.getBadge().getBadgeId().intValue() : null,// 배지 ID 추가
                profile.getTotalDistance(),
                profile.getTotalTime(),
                profile.getTotalTrash()
        );
    }

}