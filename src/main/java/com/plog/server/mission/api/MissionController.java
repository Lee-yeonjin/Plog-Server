package com.plog.server.mission.api;

import com.plog.server.global.ApiResponse;
import com.plog.server.mission.domain.Mission;
import com.plog.server.mission.dto.MissionResponse;
import com.plog.server.mission.service.MissionService;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/mission")
@RequiredArgsConstructor
@Configuration
public class MissionController {
    private final ProfileRepository profileRepository;
    private final MissionService missionService; // DailyQuestService 주입

    @GetMapping("/{uuid}/dailyquest")
    public ResponseEntity<ApiResponse> inquireMission(@PathVariable UUID uuid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        List<MissionResponse> missions = missionService.getDailyQuests(profile);

        return ResponseEntity.ok(new ApiResponse("미션 조회 성공", missions));
    }

    @PostMapping("/{uuid}/reroll")
    public ResponseEntity<ApiResponse> reroll(@PathVariable UUID uuid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        missionService.rerollUserMissions(profile);

        List<MissionResponse> dailyQuests = missionService.getDailyQuests(profile);
        return ResponseEntity.ok(new ApiResponse("다시 돌리기 성공", dailyQuests));
    }

    @PostMapping("/{uuid}/{missionid}/successful")
    public ResponseEntity<ApiResponse> successful(@PathVariable UUID uuid, @PathVariable Integer missionid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        Long missionId_Long = missionid.longValue();

        Mission mission = missionService.findMissionById(missionId_Long)
                .orElseThrow(() -> new IllegalArgumentException("미션을 찾을 수 없습니다"));

        boolean isSuccess = missionService.checkMissionSuccess(profile, mission);

        if (isSuccess) {
            missionService.completeMission(profile, mission);
            return ResponseEntity.ok(new ApiResponse("미션 성공", null));
        } else {
            return ResponseEntity.ok(new ApiResponse("미션 실패", null));
        }
    }

    @PostMapping("/{uuid}/{missionid}/advertisement")
    public ResponseEntity<ApiResponse> advertisement(@PathVariable UUID uuid, @PathVariable Integer missionid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        Long missionId_Long = missionid.longValue();

        Mission mission = missionService.findMissionById(missionId_Long)
                .orElseThrow(() -> new IllegalArgumentException("미션을 찾을 수 없습니다"));

        missionService.completeMission(profile, mission);
        return ResponseEntity.ok(new ApiResponse("미션 성공", null));
    }
}


