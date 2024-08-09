package com.plog.server.plogging.api;

import com.plog.server.global.ApiResponse;
import com.plog.server.plogging.domain.Activity;
import com.plog.server.plogging.dto.ActivityRequest;
import com.plog.server.plogging.dto.ActivityResponse;
import com.plog.server.plogging.service.ActivityService;
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

    @PostMapping("/start/{uuid}")
    public ResponseEntity<ApiResponse> startActivity(@PathVariable UUID uuid) {
        User user = activityService.startActivity(uuid);
        ApiResponse response = new ApiResponse("플로깅 시작", user);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

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

    //루트 선택
    @GetMapping("/{uuid}/{activityId}")
    public ApiResponse<ActivityResponse> getActivityByUserUUIDAndId(
            @PathVariable UUID uuid,
            @PathVariable Long activityId) {

        ActivityResponse activity = activityService.getActivityByUserUUIDAndId(uuid, activityId);
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

}