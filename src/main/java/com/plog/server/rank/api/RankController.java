package com.plog.server.rank.api;

import com.plog.server.global.ApiResponse;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.repository.ProfileRepository;
import com.plog.server.rank.service.RankService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/rank")
@RequiredArgsConstructor
@Configuration
public class RankController {
    private final ProfileRepository profileRepository;
    private final RankService rankService;

    @GetMapping("/{uuid}/inquiry")
    public ResponseEntity<ApiResponse> inquireRank(@PathVariable UUID uuid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        // 랭킹 정보와 내 점수 정보 가져오기
        Map<String, Object> rankingsData = rankService.getRankingsByProfile(profile);

        // 응답 데이터 구성
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("ranking", rankingsData.get("data")); // 기존 랭킹 데이터
        responseData.put("mydata", rankingsData.get("mydata")); // 내 데이터

        // 응답 생성
        return ResponseEntity.ok(new ApiResponse("랭킹 조회 성공", responseData));

    }
}
