package com.plog.server.badge.service;

import com.plog.server.badge.domain.Badge;
import com.plog.server.badge.domain.MyBadge;
import com.plog.server.badge.dto.BadgeResponse;
import com.plog.server.badge.repository.BadgeRepository;
import com.plog.server.badge.repository.MyBadgeRepository;
import com.plog.server.plogging.domain.Activity;
import com.plog.server.plogging.repository.ActivityRepository;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.repository.ProfileRepository;
import com.plog.server.trash.domain.Trash;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class BadgeService {
    private final BadgeRepository badgeRepository;
    private final ProfileRepository profileRepository;
    private final MyBadgeRepository myBadgeRepository;
    private final ActivityRepository activityRepository;

    public BadgeResponse getBadgeCondition(int badgeId) {
        Badge badge = badgeRepository.findById((long)badgeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 배지를 찾을 수 없습니다."));

        return new BadgeResponse(badge.getBadgeId().intValue(), badge.getBadgeGoal(), badge.getCost());
    }

    //배지 해금
    @Transactional
    public String checkBadgeConditions(UUID uuid, int badgeId) {
        // 사용자 프로필 조회
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 없습니다: " + uuid));

        // 배지 조회
        Badge badge = badgeRepository.findById((long) badgeId)
                .orElseThrow(() -> new IllegalArgumentException("배지가 없습니다: " + badgeId));

        // 배지 해금 조건 체크
        boolean isEligible = checkConditions(profile, badgeId);

        if (isEligible) {
            // 배지 생성 및 활성화
            MyBadge newMyBadge = MyBadge.builder()
                    .profile(profile)
                    .badge(badge)
                    .myBadgeStatus(false)
                    .build();
            myBadgeRepository.save(newMyBadge);

            return "배지가 성공적으로 해금되었습니다.";
        } else {
            return "조건을 충족하지 않습니다.";
        }
    }

    private boolean checkConditions(Profile profile, int badgeId) {
        switch (badgeId) {
            case 2: // "사자 : 플로깅 50시간 달성"
                return profile.getTotalTime() >= 50;
            case 3: // "북극곰 : 플로깅 100시간 달성"
                return profile.getTotalTime() >= 100;
            case 4: // "황새 : 코인 1000000개 획득"
                return profile.getTotalCoin() >= 1000000;
            case 5: // "작은발톱수달 : 달린거리 50km 달성"
                return profile.getTotalDistance() >= 50;
            case 6: // "붉은여우 : 달린거리 100km 달성"
                return profile.getTotalDistance() >= 100;
            case 7: // "철갑상어 : 플로깅 활동 10번 달성"
                return getActivityCount(profile) >= 10;
            case 8: // "혹등고래 : 플로깅 활동 100번 달성"
                return getActivityCount(profile) >= 100;
            case 9: // "코끼리 : 루트 등록 10회 달성"
                return getRouteCount(profile) >= 10;
            case 10: // "호랑이 : 루트 등록 100회 달성"
                return getRouteCount(profile) >= 100;
            case 12: // "일반쓰레기 초급 – 일반쓰레기 50개 줍기"
                return getTrashCount(profile, "일반쓰레기") >= 50;
            case 13: // "일반쓰레기 중급 – 일반쓰레기 200개 줍기"
                return getTrashCount(profile, "일반쓰레기") >= 200;
            case 14: // "일반쓰레기 고급 – 일반쓰레기 500개 줍기"
                return getTrashCount(profile, "일반쓰레기") >= 500;
            case 15: // "플라스틱 초급 – 플라스틱 50개 줍기"
                return getTrashCount(profile, "플라스틱") >= 50;
            case 16: // "플라스틱 중급 – 플라스틱 200개 줍기"
                return getTrashCount(profile, "플라스틱") >= 200;
            case 17: // "플라스틱 고급 – 플라스틱 500개 줍기"
                return getTrashCount(profile, "플라스틱") >= 500;
            default:
                return false;
        }
    }

    private int getActivityCount(Profile profile) {
        List<Activity> activities = activityRepository.findByProfile(profile);
        return activities.size();
    }
    private int getRouteCount(Profile profile) {
        return activityRepository.countByProfileAndRouteStatus(profile, true);
    }
    private int getTrashCount(Profile profile, String trashType) {
        List<Activity> activities = activityRepository.findByProfile(profile);
        int totalCount = 0;

        for (Activity activity : activities) {
            Trash trash = activity.getTrash();
            if (trash != null) {
                switch (trashType) {
                    case "일반쓰레기":
                        totalCount += trash.getGarbage();
                        break;
                    case "플라스틱":
                        totalCount += trash.getPlastic();
                        break;
                    default:
                        throw new IllegalArgumentException("알 수 없는 쓰레기 종류: " + trashType);
                }
            }
        }

        return totalCount;
    }

}
