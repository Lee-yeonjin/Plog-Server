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
    public ResponseEntity<ApiResponse> inquireRank(@PathVariable UUID uuid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        List<Mission> missions = missionService.getDailyQuests(profile);

        return ResponseEntity.ok(new ApiResponse("미션 조회 성공", missions));
    }

    @PostMapping("/{uuid}/reroll")
    public ResponseEntity<ApiResponse> reroll(@PathVariable UUID uuid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        missionService.rerollUserMissions(profile);

        List<Mission> dailyQuests = missionService.getDailyQuests(profile);
        return ResponseEntity.ok(new ApiResponse("다시 돌리기 성공", dailyQuests));
    }
}


