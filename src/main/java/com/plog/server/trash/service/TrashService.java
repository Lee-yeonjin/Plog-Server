package com.plog.server.trash.service;

import com.plog.server.global.util.s3File.dto.S3FileResponse;
import com.plog.server.global.util.s3File.service.S3UploaderService;
import com.plog.server.plogging.domain.Activity;
import com.plog.server.plogging.domain.ActivityPhoto;
import com.plog.server.plogging.repository.ActivityPhotoRepository;
import com.plog.server.plogging.repository.ActivityRepository;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.repository.ProfileRepository;
import com.plog.server.rank.service.RankCreationService;
import com.plog.server.trash.domain.Trash;
import com.plog.server.trash.dto.TrashRequest;
import com.plog.server.trash.dto.TrashResponse;
import com.plog.server.trash.repository.TrashRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TrashService {
    private final TrashRepository trashRepository;
    private final RankCreationService rankCreationService;
    private final ProfileRepository profileRepository;
    private final S3UploaderService s3UploaderService;
    private final ActivityPhotoRepository activityPhotoRepository;
    private final ActivityRepository activityRepository;

    public boolean hasCollectedPlasticToday(Profile profile, int requiredCount) {
        List<Activity> activities = activityRepository.findByProfileAndPloggingDate(profile, LocalDate.now());

        int totalPlasticCollected = activities.stream()
                .map(activity -> trashRepository.findByActivity(activity))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .mapToInt(Trash::getPlastic)
                .sum();

        return totalPlasticCollected >= requiredCount; // 15개 이상 수집된 경우 true 반환
    }

    public boolean hasCollectedGarbageToday(Profile profile, int requiredCount) {
        // 오늘 날짜의 활동을 가져옵니다.
        List<Activity> activities = activityRepository.findByProfileAndPloggingDate(profile, LocalDate.now());

        int totalPlasticCollected = activities.stream()
                .map(activity -> trashRepository.findByActivity(activity))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .mapToInt(Trash::getGarbage)
                .sum();

        return totalPlasticCollected >= requiredCount; // 15개 이상 수집된 경우 true 반환
    }

    public S3FileResponse createTrash(TrashRequest trashRequest, Activity activity, UUID uuid, MultipartFile photo) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        S3FileResponse s3FileResponse = new S3FileResponse("", "");

        if (photo != null && !photo.isEmpty()) {
            try {
                // 파일 업로드 및 URL 생성
                s3FileResponse = s3UploaderService.uploadFile(photo, "activity-photos/");

                // ActivityPhoto 엔티티 생성
                ActivityPhoto activityPhoto = ActivityPhoto.builder()
                        .activity(activity)
                        .activityPhotoName(photo.getOriginalFilename())
                        .actitityPhotoS3Key(s3FileResponse.getS3FileName())
                        .build();

                activityPhotoRepository.save(activityPhoto);
            } catch (Exception e) {
                s3FileResponse = new S3FileResponse("", "");
            }
        }

        Trash trash = Trash.builder()
                .garbage(trashRequest.getGarbage())
                .can(trashRequest.getCan())
                .plastic(trashRequest.getPlastic())
                .paper(trashRequest.getPaper())
                .plastic_bag(trashRequest.getPlastic_bag())
                .glass(trashRequest.getGlass())
                .activity(activity)
                .build();

        Trash savedTrash = trashRepository.save(trash); // Trash 저장
        // Rank 생성
        rankCreationService.createRank(activity, savedTrash); // Rank 생성

        profile.setTotalTrash(profile.getTotalTrash() + savedTrash.getTrash_sum());
        profileRepository.save(profile);

        return s3FileResponse;
    }

    public TrashResponse getTrashDetails(UUID uuid, Activity activity) {
        profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        Trash trash = trashRepository.findByActivity(activity)
                .orElseThrow(() -> new IllegalArgumentException("No trash found for the activity"));

        String activityUrl = getActivityUrl(activity);

        return TrashResponse.builder()
                .trashId(trash.getTrashId())
                .garbage(trash.getGarbage())
                .can(trash.getCan())
                .plastic(trash.getPlastic())
                .paper(trash.getPaper())
                .plastic_bag(trash.getPlastic_bag())
                .glass(trash.getGlass())
                .trashSum(trash.getTrash_sum())
                .ploggingDate(trash.getActivity().getPloggingDate())
                .photo(activityUrl)
                .build();
    }

    //사진 조회 url
    private String getActivityUrl(Activity activity) {
        return Optional.ofNullable(activityPhotoRepository.findByActivity_ActivityId(activity.getActivityId()))
                .map(clubMainPhoto -> s3UploaderService.generatePresignedGetUrl(clubMainPhoto.getActitityPhotoS3Key()))
                .orElse("none");
    }
}