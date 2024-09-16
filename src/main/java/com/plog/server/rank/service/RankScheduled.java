package com.plog.server.rank.service;

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

@Service
@RequiredArgsConstructor
@Slf4j
@Component
@EnableScheduling
public class RankScheduled {
    private final RankRepository rankRepository;

    @Scheduled(cron = "0 17 1 * * MON")
    @Transactional
    public void resetRankings() {
        log.info("Resetting rankings...");
        List<Rank> ranks = rankRepository.findAll();
        for (Rank rank : ranks) {
            rank.setDistance(0.0);
            rank.setTrash_sum(0);
        }
        rankRepository.saveAll(ranks); // 변경된 랭크 정보 저장
    }
}