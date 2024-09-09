package com.plog.server.global;

import com.plog.server.badge.domain.Badge;
import com.plog.server.badge.domain.MyBadge;
import com.plog.server.badge.repository.BadgeRepository;
import com.plog.server.badge.repository.MyBadgeRepository;
import com.plog.server.plogging.domain.Activity;
import com.plog.server.plogging.domain.Location;
import com.plog.server.plogging.repository.ActivityRepository;
import com.plog.server.plogging.repository.LocationRepository;
import com.plog.server.post.domain.Post;
import com.plog.server.post.repository.PostRepository;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.repository.ProfileRepository;
import com.plog.server.user.domain.User;
import com.plog.server.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DummyData {
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final ProfileRepository profileRepository;
    private final BadgeRepository badgeRepository;
    private final MyBadgeRepository myBadgeRepository;
    private final ActivityRepository activityRepository;
    private final PostRepository postRepository;

    @PostConstruct
    public void init (){
        User user = User.builder()
                .userAccount("user")
                .userPw("1234")
                .userEmail("email")
                .userUUID(UUID.randomUUID())
                .build();
        userRepository.save(user);

        Badge badge = Badge.builder()
                .badgeGoal("기본 배지")
                .cost(0)
                .build();
        badgeRepository.save(badge);

        Profile profile = Profile.builder()
                .userNickname("메롱")
                .user(user)
                .badge(badge)
                .totalTrash(0)
                .totalTime(0.0)
                .totalCoin(0)
                .totalDistance(0.0)
                .userMembership(false)
                .ploggingStatus(false)
                .build();
        profileRepository.save(profile);

        MyBadge myBadge = MyBadge.builder()
                .badge(badge)
                .profile(profile)
                .myBadgeStatus(true)
                .build();
        myBadgeRepository.save(myBadge);

        log.info("주입성공");

        Activity activity = Activity.builder()
                .profile(profile)
                .ploggingTime(30) // 예시: 30분
                .distance(1.0) // 예시: 1km
                .routeStatus(false)
                .startPlace("시작 장소")
                .endPlace("종료 장소")
                .ploggingDate(LocalDate.now())
                .build();
        activityRepository.save(activity);
        log.info("활동 정보 저장");

        Location location = Location.builder()
                .profile(profile)
                .longitude(123)
                .latitude(123)
                .activity(activity)
                .build();

        locationRepository.save(location);

        Location location2 = Location.builder()
                .profile(profile)
                .longitude(124)
                .latitude(124)
                .activity(activity)
                .build();

        locationRepository.save(location2);

        Location location3 = Location.builder()
                .profile(profile)
                .longitude(125)
                .latitude(125)
                .activity(activity)
                .build();

        locationRepository.save(location3);

        log.info("위치 정보 저장");

        Post post = Post.builder()
                .title("더미 포스트 제목 ")
                .content("이것은 더미 포스트 내용입니다. 포스트 번호: ")
                .joinCount(0)
                .likeCount(0)
                .plogPlace("장소 " )
                .meetPlace("모임 장소 " )
                .time(LocalDate.now())
                .schedule("2024-09-01")
                .profile(profile) // Associate the post with the created profile
                .build();
        postRepository.save(post);
        log.info("더미 포스트 데이터 저장 완료");

        User user2 = User.builder()
                .userAccount("user2")
                .userPw("1234")
                .userEmail("email2")
                .userUUID(UUID.randomUUID())
                .build();
        userRepository.save(user2);

        Badge badge2 = Badge.builder()
                .badgeGoal("기본 배지")
                .cost(0)
                .build();
        badgeRepository.save(badge2);

        Profile profile2 = Profile.builder()
                .userNickname("메롱2")
                .user(user2)
                .badge(badge)
                .totalTrash(0)
                .totalTime(0.0)
                .totalCoin(0)
                .totalDistance(0.0)
                .userMembership(false)
                .ploggingStatus(false)
                .build();
        profileRepository.save(profile2);

        MyBadge myBadge2 = MyBadge.builder()
                .badge(badge2)
                .profile(profile2)
                .myBadgeStatus(true)
                .build();
        myBadgeRepository.save(myBadge2);

        User user3 = User.builder()
                .userAccount("user3")
                .userPw("1234")
                .userEmail("email3")
                .userUUID(UUID.randomUUID())
                .build();
        userRepository.save(user3);

        Badge badge3 = Badge.builder()
                .badgeGoal("기본 배지")
                .cost(0)
                .build();
        badgeRepository.save(badge3);

        Profile profile3 = Profile.builder()
                .userNickname("메롱3")
                .user(user3)
                .badge(badge)
                .totalTrash(0)
                .totalTime(0.0)
                .totalCoin(0)
                .totalDistance(0.0)
                .userMembership(false)
                .ploggingStatus(false)
                .build();
        profileRepository.save(profile3);

        MyBadge myBadge3 = MyBadge.builder()
                .badge(badge3)
                .profile(profile3)
                .myBadgeStatus(true)
                .build();
        myBadgeRepository.save(myBadge3);
        log.info("주입성공");

    }

}
