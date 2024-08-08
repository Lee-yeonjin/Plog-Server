package com.plog.server.plogging.service;

import com.plog.server.plogging.domain.Activity;
import com.plog.server.plogging.domain.Location;
import com.plog.server.plogging.dto.ActivityRequest;
import com.plog.server.plogging.dto.ActivityResponse;
import com.plog.server.plogging.repository.ActivityRepository;
import com.plog.server.plogging.repository.LocationRepository;
import com.plog.server.user.domain.User;
import com.plog.server.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;

    @Transactional
    public User startActivity(UUID uuid) {
        // 사용자 조회
        User user = userRepository.findByUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found with UUID: " + uuid));
        ;

        // 사용자 플로깅 상태 업데이트
        user.setUserPloggingStatus(true);
        userRepository.save(user);

        return user;
    }

    @Transactional
    public ActivityResponse endActivity(UUID uuid, ActivityRequest activityRequest) {
        // 사용자 조회
        User user = userRepository.findByUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found with UUID: " + uuid));
        ;

        // 사용자 플로깅 상태 업데이트
        user.setUserPloggingStatus(false);
        userRepository.save(user);

        // 사용자와 연관된 아직 Activity와 연결되지 않은 Location 조회
        List<Location> locations = locationRepository.findByUserUserUUIDAndActivityIsNull(uuid);

        // 새로운 활동 생성
        Activity activity = Activity.builder()
                .user(user)
                .ploggingTime(activityRequest.getAcitvityTime())
                .distance(activityRequest.getDistance())
                .locations(locations) // 위치 정보 저장
                .build();
        // 액티비티 저장
        activityRepository.save(activity);

        // 각 Location의 Activity를 설정하고 다시 저장
        for (Location location : locations) {
            location.setActivity(activity);
            locationRepository.save(location);
        }

        return new ActivityResponse(activity);
    }

    public List<ActivityResponse> getActivitiesByUserUUID(UUID userUUID) {
        // 사용자 조회
        User user = userRepository.findByUserUUID(userUUID)
                .orElseThrow(() -> new IllegalArgumentException("User not found with UUID: " + userUUID));
        ;

        // 사용자의 모든 액티비티 조회
        List<Activity> activities = activityRepository.findByUser(user);

        // ActivityResponse로 변환하여 반환
        return activities.stream()
                .map(ActivityResponse::new)
                .collect(Collectors.toList());
    }

}
