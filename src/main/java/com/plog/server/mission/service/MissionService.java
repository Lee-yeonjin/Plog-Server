package com.plog.server.mission.service;

import com.plog.server.mission.domain.Mission;
import com.plog.server.mission.domain.UserMission;
import com.plog.server.mission.repository.MissionRepository;
import com.plog.server.mission.repository.UserMissionRepository;
import com.plog.server.profile.domain.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MissionService {
    private final UserMissionRepository userMissionRepository;
    private final MissionRepository missionRepository;

    // 미션 정보 불러오기
    public List<Mission> getDailyQuests(Profile profile) {
        List<UserMission> userMissions = userMissionRepository.findByProfile(profile);
        LocalDateTime now = LocalDateTime.now();
        boolean isNewDay = userMissions.isEmpty() || userMissions.stream()
                .noneMatch(um -> um.getCreatedDay().isEqual(now.toLocalDate()));

        if (isNewDay) {
            List<Mission> allMissions = missionRepository.findAll();
            Collections.shuffle(allMissions);

            int missionCount = profile.isUserMembership() ? 6 : 3; // userMembership이 true면 6개, 아니면 3개
            List<Mission> selectedMissions = allMissions.stream().limit(missionCount).collect(Collectors.toList());

            saveUserMissions(profile, selectedMissions, now); // 미션 저장 메서드 호출

            return selectedMissions; // 원래 코인 값으로 미션 목록 반환
        } else {
            // 유저 미션에서 미션 가져오기
            List<Mission> missions = userMissions.stream()
                    .map(userMission -> {
                        Mission mission = userMission.getMission();
                        mission.setMissionCoin(userMission.getMissionCoin()); // 유저 미션의 코인 값 사용
                        return mission;
                    })
                    .collect(Collectors.toList());

            return missions; // 유저 미션 테이블에서 가져온 미션 목록 반환
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

    // 리롤버튼
    public void rerollUserMissions(Profile profile) {
        List<UserMission> userMissions = userMissionRepository.findByProfile(profile);
        userMissionRepository.deleteAll(userMissions);

        createUserMission(profile);
    }
}