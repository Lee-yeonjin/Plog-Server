package com.plog.server.plogging.service;

import com.plog.server.plogging.domain.Activity;
import com.plog.server.plogging.domain.Location;
import com.plog.server.plogging.dto.ActivityRequest;
import com.plog.server.plogging.dto.ActivityResponse;
import com.plog.server.plogging.repository.ActivityRepository;
import com.plog.server.plogging.repository.LocationRepository;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final LocationRepository locationRepository;
    private final GeocodeService geocodeService;
    private final ProfileRepository profileRepository;

    @Transactional
    public Profile startActivity(UUID uuid) {

        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 없습니다" + uuid));

        // 사용자 플로깅 상태 업데이트
        profile.setPloggingStatus(true);
        profileRepository.save(profile);

        return profile;
    }

    @Transactional
    public ActivityResponse endActivity(UUID uuid, ActivityRequest activityRequest) {

        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 없습니다" + uuid));

        // 사용자 플로깅 상태 업데이트
        profile.setPloggingStatus(false);
        profileRepository.save(profile);

        // 사용자와 연관된 아직 Activity와 연결되지 않은 Location 조회
        List<Location> locations = locationRepository.findByProfileAndActivityIsNull(profile);

        // 주소 정보 설정
        String startPlace = geocodeService.getAddress(activityRequest.getLatitude1(), activityRequest.getLongitude1());
        String endPlace = geocodeService.getAddress(activityRequest.getLatitude2(), activityRequest.getLongitude2());

        // 새로운 활동 생성
        Activity activity = Activity.builder()
                .profile(profile)
                .ploggingTime(activityRequest.getAcitvityTime())
                .distance(activityRequest.getDistance())
                .locations(locations) // 위치 정보 저장
                .startPlace(startPlace)
                .endPlace(endPlace)
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

    //사용자 활동 조회
    public List<ActivityResponse> getActivitiesByUserUUID(UUID uuid) {

        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 없습니다" + uuid));


        // 사용자의 모든 액티비티 조회
        List<Activity> activities = activityRepository.findByProfile(profile);

        // ActivityResponse로 변환하여 반환
        return activities.stream()
                .map(ActivityResponse::new)
                .collect(Collectors.toList());
    }

    //활동 상세 조회
    public ActivityResponse getActivityByUserUUIDAndId(UUID uuid, Long activityId) {
        // 사용자 조회
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 없습니다" + uuid));

        // 특정 활동 조회
        Activity activity = activityRepository.findByProfileAndActivityId(profile, activityId)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found with ID: " + activityId));

        // ActivityResponse로 변환하여 반환
        return new ActivityResponse(activity);
    }

    //루트 등록
    public Boolean setRouteStatus(UUID uuid,  Long activityId) {

        // Profile과 Activity를 조인하여 해당 조건에 맞는 Activity를 찾기
        Activity activity = activityRepository.findByActivityIdAndProfileUserUserUUID(activityId, uuid)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found for given UUID and activityId: " + uuid + ", " + activityId));

        // routeStatus를 true로 설정
        activity.setRouteStatus();

        // 변경된 액티비티 저장
        activityRepository.save(activity);

        return true;
    }

    //등록된 루트 목록 조회
    // routeStatus가 true인 모든 액티비티 조회
    public List<ActivityResponse> getAllRoute() {
        List<Activity> activeActivities = activityRepository.findByRouteStatus(true);
        return activeActivities.stream()
                .map(ActivityResponse::new)
                .collect(Collectors.toList());
    }

    //사용자가 등록한 루트 목룍 조회
    // 특정 사용자의 routeStatus가 true인 모든 액티비티 조회
    public List<ActivityResponse> getAllRouteByUser(UUID uuid) {
        // 사용자 조회
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("프로필이 없습니다" + uuid));

        List<Activity> activeActivities = activityRepository.findByProfileAndRouteStatus(profile, true);
        return activeActivities.stream()
                .map(ActivityResponse::new)
                .collect(Collectors.toList());
    }

}