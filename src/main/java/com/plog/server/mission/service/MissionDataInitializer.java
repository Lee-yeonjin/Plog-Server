package com.plog.server.mission.service;

import com.plog.server.mission.domain.Mission;
import com.plog.server.mission.repository.MissionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MissionDataInitializer implements CommandLineRunner {
    private final MissionRepository missionRepository; // 오타 수정

    public MissionDataInitializer(MissionRepository missionRepository) { // 생성자 추가
        this.missionRepository = missionRepository; // 오타 수정
    }

    @Override
    public void run(String... args) throws Exception {
        // 기본 Mission 데이터 삽입
        List<Mission> missions = Arrays.asList(
                Mission.builder().missionCoin(1).mission("오늘 플로깅 1회 하기").build(),
                Mission.builder().missionCoin(2).mission("플라스틱 15개 줍기").build(),
                Mission.builder().missionCoin(3).mission("나만의 루트 만들기").build(),
                Mission.builder().missionCoin(1).mission("커뮤니티 글 올리기").build(),
                Mission.builder().missionCoin(3).mission("일반쓰레기 20개 줍기").build(),
                Mission.builder().missionCoin(2).mission("프로필 배지 변경하기").build(),
                Mission.builder().missionCoin(3).mission("플로깅 후 사진 업로드하기").build(),
                Mission.builder().missionCoin(2).mission("지역 플로깅 모임 참여하기").build(),
                Mission.builder().missionCoin(1).mission("아몰랑").build()
        );

        for (Mission mission : missions) {
            missionRepository.save(mission); // Mission 데이터 저장
        }
    }
}
