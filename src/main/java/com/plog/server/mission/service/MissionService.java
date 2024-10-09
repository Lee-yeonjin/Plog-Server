package com.plog.server.mission.service;

import com.plog.server.mission.domain.Mission;
import com.plog.server.mission.domain.UserMission;
import com.plog.server.mission.dto.MissionResponse;
import com.plog.server.mission.repository.MissionRepository;
import com.plog.server.mission.repository.UserMissionRepository;
import com.plog.server.plogging.service.ActivityService;
import com.plog.server.post.service.PostService;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.repository.ProfileRepository;
import com.plog.server.trash.service.TrashService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.metadata.DataSourcePoolMetadataProvidersConfiguration;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MissionService {
    private final UserMissionRepository userMissionRepository;
    private final MissionRepository missionRepository;
    private final ProfileRepository profileRepository;
    private final ActivityService activityService;
    private final PostService postService;
    private final TrashService  trashService;
    private final DataSourcePoolMetadataProvidersConfiguration dataSourcePoolMetadataProvidersConfiguration;

    // 미션 정보 불러오기
    public List<MissionResponse> getDailyQuests(Profile profile) {
        List<UserMission> userMissions = userMissionRepository.findByProfile(profile);
        LocalDateTime now = LocalDateTime.now();
        boolean isNewDay = userMissions.isEmpty() || userMissions.stream()
                .noneMatch(um -> um.getCreatedDay().isEqual(now.toLocalDate()));

        if (isNewDay) {
            List<Mission> allMissions = missionRepository.findAll();
            Collections.shuffle(allMissions);

            int missionCount = profile.isUserMembership() ? 6 : 3;
            List<Mission> selectedMissions = allMissions.stream().limit(missionCount).collect(Collectors.toList());

            saveUserMissions(profile, selectedMissions, now); // 미션 저장 메서드 호출

            return selectedMissions.stream()
                    .map(mission -> {
                        UserMission userMission = new UserMission();
                        userMission.setProfile(profile);
                        userMission.setMission(mission); // Mission 객체를 설정
                        userMission.setMissionCoin(mission.getMissionCoin());
                        userMission.setCreatedDay(now.toLocalDate());
                        userMission.setFinish(false); // 기본적으로 false로 설정

                        int missionId = userMission.getMission().getMissionId().intValue();

                        return new MissionResponse("미션 조회 성공", missionId, userMission.getMissionCoin(), userMission.isFinish());
                    })
                    .collect(Collectors.toList());
        } else {
            return userMissions.stream()
                    .map(userMission -> {
                        int missionId = userMission.getMission().getMissionId().intValue();

                        String message = userMission.getMission().getMission();

                        return new MissionResponse(message, missionId, userMission.getMissionCoin(), userMission.isFinish());
                    })
                    .collect(Collectors.toList());
        }
    }

    // 사용자의 미션이 존재하는지 확인여부
    public boolean checkUserMissionExists(Profile profile) {
        if (profile == null) {
            return false; // 사용자가 로그인하지 않은 경우
        }
        List<UserMission> missions = userMissionRepository.findByProfile(profile);
        return !missions.isEmpty();
    }

    // 미션 생성
    public void createUserMission(Profile profile) {
        LocalDateTime now = LocalDateTime.now();

        // 랜덤으로 미션 선택
        List<Mission> allMissions = missionRepository.findAll();
        Collections.shuffle(allMissions);

        // userMembership에 따라 선택할 미션 개수 결정
        int missionCount = profile.isUserMembership() ? 6 : 3; // userMembership이 true면 6개, 아니면 3개
        List<Mission> selectedMissions = allMissions.stream().limit(missionCount).collect(Collectors.toList());

        // 미션 저장 메서드 호출
        saveUserMissions(profile, selectedMissions, now);
    }

    // 미션 저장
    private void saveUserMissions(Profile profile, List<Mission> selectedMissions, LocalDateTime now) {
        for (Mission mission : selectedMissions) {
            // 미션이 저장되지 않았다면 저장
            if (mission.getMissionId() == null) {
                missionRepository.save(mission);
            }

            UserMission userMission = new UserMission();
            userMission.setProfile(profile);
            userMission.setMission(mission);
            userMission.setMissionCoin(mission.getMissionCoin());
            userMission.setCreatedDay(now.toLocalDate());

            // 멤버십 가입자일 경우 코인을 2배로 설정
            if (profile.isUserMembership()) {
                userMission.setMissionCoin(mission.getMissionCoin() * 2);
            }

            userMissionRepository.save(userMission);
        }
    }

    public void completeMission(Profile profile, Mission mission) {
        UserMission userMission = userMissionRepository.findByProfileAndMission(profile, mission)
                .orElseThrow(() -> new IllegalArgumentException("UserMission not found"));

        // 미션의 코인을 프로필의 총 코인에 추가
        profile.setIncreaseCoins(userMission.getMissionCoin());

        // 미션 상태를 완료로 변경
        userMission.setFinish(true); // 성공 상태 업데이트
        userMissionRepository.save(userMission); // UserMission 저장

        // 업데이트된 프로필 저장
        profileRepository.save(profile);
    }

    // 미션 ID로 미션 찾기
    public Optional<Mission> findMissionById(Long missionId) {
        return missionRepository.findById(missionId);
    }

    // 미션 성공 여부 확인
    public boolean checkMissionSuccess(Profile profile, Mission mission) {
        switch (mission.getMissionId().intValue()) { // 미션 ID를 int로 변환하여 사용
            case 1: // 오늘 플로깅 1회 하기
                return activityService.hasPloggedToday(profile);
            case 2: // 플라스틱 15개 이상 줍기
                return trashService.hasCollectedPlasticToday(profile, 15);
            case 3: // 나만의 루트 만들기
                return activityService.hasCreatedRouteToday(profile);
            case 4: // 오늘 커뮤니티에 글 작성
                return postService.checkPostsByProfileToday(profile);
            case 5: // 일반쓰레기 20개 줍기
                return trashService.hasCollectedGarbageToday(profile,20);
            case 6: // 활동시간 최고기록 갱신
                Integer todayDuration = activityService.getTodayBestPloggingDuration(profile); // 오늘 플로깅 시간
                Integer previousBest = activityService.getPreviousBestPloggingDuration(profile); // 이전 최고 기록

                if (todayDuration > previousBest) {
                    updateMissionStatus(profile, mission.getMissionId(), true); // 성공 업데이트
                    return true;
                } else {
                    return false;
                }
            case 7: // 플로깅 후 사진 업로드 하기
                return activityService.hasUploadedPhoto(profile);
            case 8: // 3km 이상 활동하기
                return activityService.hasPloggedMoreThan3KmToday(profile);
            case 9: // 30뷴 이상 활동하기
                return activityService.hasPloggedMoreThan30Minutes(profile);
            default:
                return false; // 다른 미션에 대해서는 기본적으로 실패로 처리
        }
    }

    public void updateMissionStatus(Profile profile, Long missionId, boolean isFinish) {
        UserMission userMission = userMissionRepository.findByProfileAndMission_MissionId(profile, missionId)
                .orElseThrow(() -> new IllegalArgumentException("해당 미션을 찾을 수 없습니다."));

        userMission.setFinish(isFinish);
        userMissionRepository.save(userMission);
    }

    // 리롤버튼
    public void rerollUserMissions(Profile profile) {
        List<UserMission> userMissions = userMissionRepository.findByProfile(profile);
        userMissionRepository.deleteAll(userMissions);

        createUserMission(profile);
    }
}