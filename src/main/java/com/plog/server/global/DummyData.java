package com.plog.server.global;

import com.plog.server.plogging.domain.Location;
import com.plog.server.plogging.repository.LocationRepository;
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

    @PostConstruct
    public void init (){
        User user = User.builder()
                .userAccount("user")
                .userPw("1234")
                .userEmail("email")
                .userNickname("플로거")
                .userUUID(UUID.randomUUID())
                .build();

        userRepository.save(user);
        log.info("주입성공");

        Location location = Location.builder()
                .user(user)
                .longitude(123)
                .latitude(123)
                .build();

        locationRepository.save(location);

        Location location2 = Location.builder()
                .user(user)
                .longitude(124)
                .latitude(124)
                .build();

        locationRepository.save(location2);

        Location location3 = Location.builder()
                .user(user)
                .longitude(125)
                .latitude(125)
                .build();

        locationRepository.save(location3);

        log.info("위치 정보 저장");
    }

}
