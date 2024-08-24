package com.plog.server.trash.service;

import com.plog.server.plogging.domain.Activity;
import com.plog.server.plogging.repository.ActivityRepository;
import com.plog.server.profile.domain.Profile;
import com.plog.server.trash.domain.Trash;
import com.plog.server.trash.dto.TrashRequest;
import com.plog.server.trash.dto.TrashResponse;
import com.plog.server.trash.repository.TrashRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TrashService {
    private final TrashRepository trashRepository;
    private final ActivityRepository activityRepository;

    public Trash createTrash(TrashRequest trashRequest, Activity activity, Profile profile) {

        Trash trash = Trash.builder()
                .garbage(trashRequest.getGarbage())
                .can(trashRequest.getCan())
                .plastic(trashRequest.getPlastic())
                .paper(trashRequest.getPaper())
                .plastic_bag(trashRequest.getPlastic_bag())
                .glass(trashRequest.getGlass())
                .activity(activity)
                .build();

        return trashRepository.save(trash);
    }

    public List<Trash> getAllTrash(Activity activity) {
        return trashRepository.findByActivity(activity);
    }


    public List<TrashResponse> getAllTrashResponses(Activity activity) {
        List<Trash> trashList = getAllTrash(activity); // 기존 메서드 호출

        return trashList.stream().map(trash -> {
            TrashResponse response = new TrashResponse();
            response.setTrashId(trash.getTrashId());
            response.setGarbage(trash.getGarbage());
            response.setCan(trash.getCan());
            response.setPlastic(trash.getPlastic());
            response.setPaper(trash.getPaper());
            response.setPlastic_bag(trash.getPlastic_bag());
            response.setGlass(trash.getGlass());
            response.setTrashSum(trash.getTrash_sum());
            response.setPloggingDate(activity.getPloggingDate());
            return response;
        }).collect(Collectors.toList());
    }
}