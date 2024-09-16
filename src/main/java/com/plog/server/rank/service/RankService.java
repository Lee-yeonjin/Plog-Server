package com.plog.server.rank.service;

import com.plog.server.badge.domain.MyBadge;
import com.plog.server.badge.repository.MyBadgeRepository;
import com.plog.server.profile.domain.Profile;
import com.plog.server.rank.domain.Rank;
import com.plog.server.rank.dto.RankResponse;
import com.plog.server.rank.repository.RankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Component
@EnableScheduling
public class RankService {
    private final RankRepository rankRepository;
    private final MyBadgeRepository myBadgeRepository;

    public Map<String, Object> getRankingsByProfile(Profile profile) {
        List<Rank> ranks = rankRepository.findAll(); // 모든 랭크 조회

        // 사용자별 점수 계산 및 정렬
        List<RankResponse> rankResponses = ranks.stream()
                .collect(Collectors.groupingBy(rank -> rank.getProfile().getUserNickname(),
                        Collectors.reducing(0.0,
                                rank -> rank.getDistance() * rank.getTrash_sum(),
                                Double::sum)))
                .entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())) // 점수 내림차순 정렬
                .map(entry -> {
                    Profile userProfile = ranks.stream()
                            .filter(rank -> rank.getProfile().getUserNickname().equals(entry.getKey()))
                            .findFirst()
                            .map(Rank::getProfile)
                            .orElse(null);

                    int badgeId = getActiveBadgeId(userProfile);
                    int score = (int) Math.round(entry.getValue());
                    return new RankResponse(entry.getKey(), score, 0, badgeId);
                })
                .collect(Collectors.toList());

        // 내 점수 정보 생성
        RankResponse myData = rankResponses.stream()
                .filter(rankResponse -> rankResponse.getUserNickname().equals(profile.getUserNickname()))
                .findFirst()
                .orElse(null);

        // 랭킹 순위 계산
        if (rankResponses.isEmpty() || rankResponses.stream().allMatch(r -> r.getScore() == 0)) {
            for (RankResponse rankResponse : rankResponses) {
                rankResponse.setRank(0); // 모든 사용자의 랭크를 0으로 설정
            }
            // myData가 null이 아닐 경우에만 myRank를 0으로 설정
            if (myData != null) {
                myData.setRank(0);
            }
        } else {
            for (int i = 0; i < rankResponses.size(); i++) {
                rankResponses.get(i).setRank(i + 1); // 1부터 시작하는 순위
            }
        }

        // 응답 데이터 구성
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("data", rankResponses.stream()
                .map(rankResponse -> new HashMap<String, Object>() {{
                    put("userNickname", rankResponse.getUserNickname());
                    put("score", rankResponse.getScore());
                    put("rank", rankResponse.getRank());
                    put("badge", rankResponse.getBadge());
                }}).collect(Collectors.toList()));

        // myRank가 null일 경우 0으로 설정
        responseData.put("mydata", Map.of(
                "myUsername", myData != null ? myData.getUserNickname() : profile.getUserNickname(),
                "myRank", myData != null ? myData.getRank() : 0,
                "myScore", myData != null ? myData.getScore() : 0
        ));

        return responseData;
    }


    private int getActiveBadgeId(Profile profile) {
        // 주어진 프로필에 대한 활성화된 배지 ID 조회
        List<MyBadge> activeBadges = myBadgeRepository.findMainBadgesByProfile(profile);

        return activeBadges.stream()
                .filter(MyBadge::myBadgeStatus) // 배지가 활성화된 경우만 필터링
                .findFirst()
                .map(myBadge -> myBadge.getBadge().getBadgeId().intValue())
                .orElse(1); // 기본값 1
    }
}
