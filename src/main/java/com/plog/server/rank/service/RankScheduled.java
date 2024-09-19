package com.plog.server.rank.service;

import com.plog.server.badge.domain.Badge;
import com.plog.server.badge.domain.MyBadge;
import com.plog.server.badge.repository.BadgeRepository;
import com.plog.server.badge.repository.MyBadgeRepository;
import com.plog.server.profile.domain.Profile;
import com.plog.server.rank.domain.Rank;
import com.plog.server.rank.repository.RankRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Component
@EnableScheduling
public class RankScheduled {
    private final RankRepository rankRepository;
    private final BadgeRepository badgeRepository;
    private final MyBadgeRepository myBadgeRepository;

    @Scheduled(cron = "0 0 0 * * SUN")
    @Transactional
    public void resetRankings() {
        log.info("Resetting rankings...");

        List<Rank> ranks = rankRepository.findAll();

        // 사용자별 점수 계산
        Map<Profile, Double> userScores = ranks.stream()
                .collect(Collectors.groupingBy(
                        Rank::getProfile,
                        Collectors.summingDouble(rank -> rank.getDistance() * rank.getTrash_sum())
                ));

        // 최고 점수를 가진 사용자 찾기
        Profile topProfile = userScores.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // 1등 배지 조회 및 부여
        Badge firstPlaceBadge = badgeRepository.findById(11L)
                .orElseThrow(() -> new IllegalArgumentException("1등 배지를 찾을 수 없습니다."));

        MyBadge myBadge = myBadgeRepository.findByProfileAndBadge(topProfile, firstPlaceBadge)
                .orElseGet(() -> MyBadge.builder()
                        .profile(topProfile)
                        .badge(firstPlaceBadge)
                        .myBadgeStatus(true)
                        .build());
        myBadgeRepository.save(myBadge);

        for (Rank rank : ranks) {
            rank.setDistance(0.0);
            rank.setTrash_sum(0);
        }
        rankRepository.saveAll(ranks); // 변경된 랭크 정보 저장
    }
}