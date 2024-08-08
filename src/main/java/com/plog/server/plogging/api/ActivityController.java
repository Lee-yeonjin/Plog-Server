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
@RequestMapping("/activity")
@Slf4j
public class ActivityController {
    private final ActivityService activityService;

    @PostMapping("/start/{uuid}")
    public ResponseEntity<ApiResponse> startActivity(@PathVariable UUID uuid) {
        User user = activityService.startActivity(uuid);
        ApiResponse response = new ApiResponse("플로깅 시작",user);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/end/{uuid}")
    public ApiResponse<ActivityResponse> endActivity(@PathVariable UUID uuid, @RequestBody ActivityRequest activityRequest) {
        ActivityResponse activityResponse = activityService.endActivity(uuid,activityRequest);
        return new ApiResponse("플로깅 종료",activityResponse);
    }

    //활동 조회
    @GetMapping("/user/{uuid}")
    public ApiResponse<List<ActivityResponse>> getActivitiesByUserUUID(@PathVariable UUID uuid) {
        List<ActivityResponse> activities = activityService.getActivitiesByUserUUID(uuid);
        return new ApiResponse<>("Activities fetched successfully", activities);
    }

}
