package com.plog.server.global;

import com.plog.server.plogging.domain.Location;
import com.plog.server.plogging.repository.LocationRepository;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.repository.ProfileRepository;
import com.plog.server.user.domain.User;
import com.plog.server.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DummyData {
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final ProfileRepository profileRepository;
    @PostConstruct
    public void init (){
        User user = User.builder()
                .userAccount("user")
                .userPw("1234")
                .userEmail("email")
                .userUUID(UUID.randomUUID())
                .build();
        userRepository.save(user);

        Profile profile = Profile.builder()
                .userNickname("메롱")
                .user(user)
                .build();
        profileRepository.save(profile);

        log.info("주입성공");

        Location location = Location.builder()
                .profile(profile)
                .longitude(123)
                .latitude(123)
                .build();

        locationRepository.save(location);

        Location location2 = Location.builder()
                .profile(profile)
                .longitude(124)
                .latitude(124)
                .build();

        locationRepository.save(location2);

        Location location3 = Location.builder()
                .profile(profile)
                .longitude(125)
                .latitude(125)
                .build();

        locationRepository.save(location3);

        log.info("위치 정보 저장");

    }

}
