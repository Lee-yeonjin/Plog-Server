package com.plog.server.global.data;

import com.plog.server.badge.domain.Badge;
import com.plog.server.badge.repository.BadgeRepository;
import com.plog.server.mission.domain.Mission;
import com.plog.server.mission.repository.MissionRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SeedData {
    private final BadgeRepository badgeRepository;
    private final MissionRepository missionRepository;

    @PostConstruct
    public void init(){
        initBadge();
    }

    public void initBadge() {
        badgeRepository.save(Badge.builder()
                .badgeGoal("사자 : 플로깅 50시간 달성")
                .cost(1000)
                .build());

        badgeRepository.save(Badge.builder()
                .badgeGoal("북극곰 : 플로깅 100시간 달성")
                .cost(2000)
                .build());

        badgeRepository.save(Badge.builder()
                .badgeGoal("황새 : 코인 1000000개 획득")
                .cost(3000)
                .build());

        badgeRepository.save(Badge.builder()
                .badgeGoal("작은발톱수달 : 달린거리 50km 달성")
                .cost(4000)
                .build());

        badgeRepository.save(Badge.builder()
                .badgeGoal("붉은여우 : 달린거리 100km 달성")
                .cost(5000)
                .build());

        badgeRepository.save(Badge.builder()
                .badgeGoal("철갑상어 : 플로깅 활동 10번 달성")
                .cost(6000)
                .build());

        badgeRepository.save(Badge.builder()
                .badgeGoal("혹등고래 : 플로깅 활동 100번 달성")
                .cost(7000)
                .build());

        badgeRepository.save(Badge.builder()
                .badgeGoal("코끼리 : 루트 등록 10회 달성")
                .cost(8000)
                .build());

        badgeRepository.save(Badge.builder()
                .badgeGoal("호랑이 : 루트 등록 100회 달성")
                .cost(9000)
                .build());

        badgeRepository.save(Badge.builder()
                .badgeGoal("1등배지 – 플로그 랭킹 1등")
                .cost(10000)
                .build());

        badgeRepository.save(Badge.builder()
                .badgeGoal("일반쓰레기 초급 – 일반쓰레기 50개 줍기")
                .cost(11000)
                .build());

        badgeRepository.save(Badge.builder()
                .badgeGoal("일반쓰레기 중급 – 일반쓰레기 200개 줍기")
                .cost(12000)
                .build());

        badgeRepository.save(Badge.builder()
                .badgeGoal("일반쓰레기 고급 – 일반쓰레기 500개 줍기")
                .cost(13000)
                .build());

        badgeRepository.save(Badge.builder()
                .badgeGoal("플라스틱 초급 – 플라스틱 50개 줍기")
                .cost(14000)
                .build());

        badgeRepository.save(Badge.builder()
                .badgeGoal("플라스틱 중급 – 플라스틱 200개 줍기")
                .cost(15000)
                .build());

        badgeRepository.save(Badge.builder()
                .badgeGoal("플라스틱 고급 – 플라스틱 500개 줍기")
                .cost(16000)
                .build());
    }

    @PostConstruct
    private void initMissions() {
        List<Mission> missions = Arrays.asList(
                Mission.builder().missionCoin(1).mission("오늘 플로깅 1회 하기").build(),
                Mission.builder().missionCoin(2).mission("플라스틱 15개 줍기").build(),
                Mission.builder().missionCoin(2).mission("나만의 루트 만들기").build(),
                Mission.builder().missionCoin(1).mission("커뮤니티 글 올리기").build(),
                Mission.builder().missionCoin(3).mission("일반쓰레기 20개 줍기").build(),
                Mission.builder().missionCoin(2).mission("활동 시간 최고기록 갱신").build(),
                Mission.builder().missionCoin(3).mission("플로깅 후 사진 업로드하기").build(),
                Mission.builder().missionCoin(3).mission("3km 이상 활동하기").build(),
                Mission.builder().missionCoin(3).mission("30분 이상 플로깅하기").build()
        );

        for (Mission mission : missions) {
            missionRepository.save(mission);
        }
    }
}
