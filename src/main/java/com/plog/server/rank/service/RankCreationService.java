package com.plog.server.rank.service;

import com.plog.server.plogging.domain.Activity;
import com.plog.server.profile.domain.Profile;
import com.plog.server.rank.domain.Rank;
import com.plog.server.rank.repository.RankRepository;
import com.plog.server.trash.domain.Trash;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RankCreationService {
    private final RankRepository rankRepository;

    public void createRank(Activity activity, Trash trash) {
        Profile profile = activity.getProfile();
        int trashSum = trash.getTrash_sum();
        Double distance = activity.getDistance();

        Rank rank = Rank.builder()
                .profile(profile)
                .trash_sum(trashSum)
                .distance(distance)
                .build();

        rankRepository.save(rank);
    }
}
