package com.plog.server.trash.api;

import com.plog.server.global.ApiResponse;
import com.plog.server.plogging.domain.Activity;
import com.plog.server.plogging.repository.ActivityRepository;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.repository.ProfileRepository;
import com.plog.server.trash.domain.Trash;
import com.plog.server.trash.dto.TrashRequest;
import com.plog.server.trash.dto.TrashResponse;
import com.plog.server.trash.service.TrashService;
import com.plog.server.user.domain.User;
import com.plog.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/trash")
@RequiredArgsConstructor
public class TrashController {
    private final TrashService trashService;
    private final UserService userService;
    private final ActivityRepository activityRepository;
    private final ProfileRepository profileRepository;

    @PostMapping("/{uuid}/{activityid}/record")
    public ResponseEntity<ApiResponse> createTrash(@PathVariable UUID uuid,  @PathVariable Long activityid, @RequestBody TrashRequest trashRequest) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        Activity activity = activityRepository.findById(activityid)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found"));

        Trash trash = trashService.createTrash(trashRequest, activity, profile);

        return ResponseEntity.ok(new ApiResponse(("플로깅 기록 저장 성공")));
    }

    // 플로깅 기록 확인 (활동에 대한 기록)
    @GetMapping("/{uuid}/{activityid}/plogging-check")
    public ResponseEntity<List<TrashResponse>> getAllTrash(@PathVariable UUID uuid,  @PathVariable Long activityid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        Activity activity = activityRepository.findById(activityid)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found"));

        List<TrashResponse> trashResponse = trashService.getAllTrashResponses(activity);
        return ResponseEntity.ok(trashResponse);
    }

}