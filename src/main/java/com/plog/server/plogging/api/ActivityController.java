package com.plog.server.plogging.api;

import com.plog.server.global.ApiResponse;
import com.plog.server.plogging.domain.Activity;
import com.plog.server.plogging.dto.ActivityRequest;
import com.plog.server.plogging.dto.ActivityResponse;
import com.plog.server.plogging.dto.RouteDetailResponse;
import com.plog.server.plogging.service.ActivityService;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.dto.ActiveProfileResponse;
import com.plog.server.profile.service.ProfileService;
import com.plog.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activitys")
@Slf4j
public class ActivityController {
    private final ActivityService activityService;
    private final ProfileService profileService;

    //플로깅 시작
    @PostMapping("/start/{uuid}")
    public ResponseEntity<ApiResponse> startActivity(@PathVariable UUID uuid) {
        Profile profile = activityService.startActivity(uuid);
        ApiResponse response = new ApiResponse("플로깅 시작", profile);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //플로깅 종료
    @PostMapping("/end/{uuid}")
    public ApiResponse<ActivityResponse> endActivity(@PathVariable UUID uuid, @RequestBody ActivityRequest activityRequest) {
        ActivityResponse activityResponse = activityService.endActivity(uuid, activityRequest);
        return new ApiResponse("플로깅 종료", activityResponse);
    }

    //사용자의 전체 활동 조회
    @GetMapping("/{uuid}")
    public ApiResponse<List<ActivityResponse>> getActivitiesByUserUUID(@PathVariable UUID uuid) {
        List<ActivityResponse> activities = activityService.getActivitiesByUserUUID(uuid);
        return new ApiResponse<>("Activities fetched successfully", activities);
    }

    //루트 선택시 상세 조회
    @GetMapping("/{uuid}/{activityId}")
    public ApiResponse<RouteDetailResponse> getRouteDetailByUserUUID(
            @PathVariable UUID uuid,
            @PathVariable Long activityId) {

        RouteDetailResponse activity = activityService.getRouteDetailByUserUUID(uuid, activityId);
        return new ApiResponse<>("루트 조회 성공", activity);
    }

    //루트 등록
    @PatchMapping("/{uuid}/{activityId}/route")
    public ResponseEntity<String> setRouteStatus(
            @PathVariable Long activityId,
            @PathVariable UUID uuid) {
        Boolean updated = activityService.setRouteStatus(uuid, activityId);
        if (updated) {
            return new ResponseEntity<>("루트 등록이 완료 되었습니다", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("루트 등록에 실패하였습니다.", HttpStatus.BAD_REQUEST);
        }
    }

    //전체 루트 조회
    @GetMapping("/all-route")
    public ApiResponse<List<ActivityResponse>> getActiveActivities() {
        List<ActivityResponse> activities = activityService.getAllRoute();
        return new ApiResponse<>("전체 루트 조회 성공", activities);
    }

    //사용자의 루트 조회
    @GetMapping("/route/{uuid}")
    public ApiResponse<List<ActivityResponse>> getActiveActivitiesByUser(@PathVariable UUID uuid) {
        List<ActivityResponse> activities = activityService.getAllRouteByUser(uuid);
        return new ApiResponse<>("사용자의 루트 조회 성공", activities);
    }

    //플로깅 중 현재 플로깅중인 사용자 조회
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<ActiveProfileResponse>>> getActivePloggingDetails() {
        List<ActiveProfileResponse> activeProfiles = profileService.getActivePloggingDetails();
        ApiResponse<List<ActiveProfileResponse>> response = new ApiResponse<>("현재 플로깅중인 사용자 조회 성공", activeProfiles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}