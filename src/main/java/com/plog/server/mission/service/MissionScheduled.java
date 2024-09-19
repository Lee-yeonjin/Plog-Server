package com.plog.server.mission.service;

import com.plog.server.mission.repository.UserMissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
@Component
@EnableScheduling
public class MissionScheduled {
    private final UserMissionRepository dailyQuestRepository;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void clearUserMissions() {
        log.info("Clearing UserMission records...");
        dailyQuestRepository.deleteAll();
    }
}