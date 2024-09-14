package com.plog.server.trash.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plog.server.global.ApiResponse;
import com.plog.server.global.util.s3File.dto.S3FileResponse;
import com.plog.server.global.util.s3File.service.S3UploaderService;
import com.plog.server.plogging.domain.Activity;
import com.plog.server.plogging.repository.ActivityPhotoRepository;
import com.plog.server.plogging.repository.ActivityRepository;
import com.plog.server.plogging.service.LocationService;
import com.plog.server.profile.repository.ProfileRepository;
import com.plog.server.trash.dto.TrashRequest;
import com.plog.server.trash.dto.TrashResponse;
import com.plog.server.trash.service.TrashService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/trash")
@RequiredArgsConstructor
public class TrashController {
    private final TrashService trashService;
    private final ActivityRepository activityRepository;

    //쓰레기 기록 저장 및 사진 저장
    @PostMapping("/{uuid}/{activityid}")
    public ResponseEntity<ApiResponse<S3FileResponse>> createTrash(@PathVariable UUID uuid, @PathVariable Long activityid,
                                                                   @RequestPart(value = "image", required = false) MultipartFile image,
                                                                   @RequestPart(value = "trashRequest") String trashRequestJson) {

        Activity activity = activityRepository.findById(activityid)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found"));

        TrashRequest trashRequest;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            trashRequest = objectMapper.readValue(trashRequestJson, TrashRequest.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("json처리 실패", null));
        }

        S3FileResponse s3FileResponse = trashService.createTrash(trashRequest, activity, uuid, image);

        return ResponseEntity.ok(new ApiResponse<>("success", s3FileResponse));

    }

    //플로깅 세부 조회시 쓰레기 및 사진 조회
    @GetMapping("{uuid}/{activityid}")
    public ApiResponse<TrashResponse> getTrashDetails(@PathVariable UUID uuid,  @PathVariable Long activityid){
        Activity activity = activityRepository.findById(activityid)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found"));

        TrashResponse trashResponse = trashService.getTrashDetails(uuid,activity);
        return new ApiResponse<>("쓰레기 및 사진 조회 완료", trashResponse);
    }

}