package com.plog.server.global;

import com.plog.server.badge.domain.Badge;
import com.plog.server.badge.domain.MyBadge;
import com.plog.server.badge.repository.BadgeRepository;
import com.plog.server.badge.repository.MyBadgeRepository;
import com.plog.server.plogging.domain.Activity;
import com.plog.server.plogging.domain.Location;
import com.plog.server.plogging.repository.ActivityRepository;
import com.plog.server.plogging.repository.LocationRepository;
import com.plog.server.plogging.service.GeocodeService;
import com.plog.server.post.domain.Post;
import com.plog.server.post.repository.PostRepository;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.repository.ProfileRepository;
import com.plog.server.rank.domain.Rank;
import com.plog.server.rank.dto.RankResponse;
import com.plog.server.rank.repository.RankRepository;
import com.plog.server.trash.domain.Trash;
import com.plog.server.trash.repository.TrashRepository;
import com.plog.server.user.domain.User;
import com.plog.server.user.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

import static java.lang.Boolean.TRUE;

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
    private final GeocodeService geocodeService;
    private final RankRepository rankRepository;
    private final TrashRepository trashRepository;

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
                .userMembership(true)
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
                .ploggingTime(120)
                .distance(5.0)
                .startPlace(geocodeService.getAddress(37.598769835423475,126.91542467032245))
                .endPlace(geocodeService.getAddress(37.59587287526493,126.91504295184949))
                .routeStatus(true)
                .ploggingDate(LocalDate.now())
                .build();
        activityRepository.save(activity);

        Location startlocation = Location.builder()
                .profile(profile)
                .longitude(126.91542467032245)
                .latitude(37.598769835423475)
                .activity(activity)
                .build();

        locationRepository.save(startlocation);

        Location location = Location.builder()
                .profile(profile)
                .longitude(126.91534860669759)
                .latitude(37.59844091925552)
                .activity(activity)
                .build();

        locationRepository.save(location);

        Location location2 = Location.builder()
                .profile(profile)
                .longitude(126.91527253863138)
                .latitude(37.59811650797162)
                .activity(activity)
                .build();

        locationRepository.save(location2);

        Location location3 = Location.builder()
                .profile(profile)
                .longitude(126.91508093028813 )
                .latitude(37.59732800321048)
                .activity(activity)
                .build();

        locationRepository.save(location3);

        Location endlocation = Location.builder()
                .profile(profile)
                .longitude(126.91504295184949)
                .latitude(37.59587287526493)
                .activity(activity)
                .build();

        locationRepository.save(endlocation);

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
                .profile(profile)
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

        Profile profile2 = Profile.builder()
                .userNickname("nickname")
                .user(user2)
                .badge(badge)
                .totalTrash(30)
                .totalTime(30.0)
                .totalCoin(30)
                .totalDistance(30.0)
                .userMembership(false)
                .ploggingStatus(true)
                .build();
        profileRepository.save(profile2);

        Badge badge2 = Badge.builder()
                .badgeGoal("기본 배지")
                .cost(0)
                .build();
        badgeRepository.save(badge2);

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

        Profile profile3 = Profile.builder()
                .userNickname("플로거")
                .user(user3)
                .badge(badge)
                .totalTrash(50)
                .totalTime(50.0)
                .totalCoin(50)
                .totalDistance(50.0)
                .userMembership(false)
                .ploggingStatus(true)
                .build();
        profileRepository.save(profile3);

        Badge badge3 = Badge.builder()
                .badgeGoal("기본 배지")
                .cost(0)
                .build();
        badgeRepository.save(badge3);

        MyBadge myBadge3 = MyBadge.builder()
                .badge(badge3)
                .profile(profile3)
                .myBadgeStatus(true)
                .build();
        myBadgeRepository.save(myBadge3);

        Activity activity2 = Activity.builder()
                .profile(profile2)
                .ploggingTime(120)
                .distance(5.0)
                .startPlace(geocodeService.getAddress(37.598769835423475,126.91542467032245))
                .endPlace(geocodeService.getAddress(37.59587287526493,126.91504295184949))
                .routeStatus(true)
                .ploggingDate(LocalDate.now())
                .build();
        activityRepository.save(activity2);

        Location startlocation2 = Location.builder()
                .profile(profile2)
                .longitude(126.91542467032245)
                .latitude(37.598769835423475)
                .activity(activity)
                .build();

        locationRepository.save(startlocation2);

        Location location4 = Location.builder()
                .profile(profile2)
                .longitude(126.91534860669759)
                .latitude(37.59844091925552)
                .activity(activity2)
                .build();

        locationRepository.save(location4);

        Location location5 = Location.builder()
                .profile(profile2)
                .longitude(126.91527253863138)
                .latitude(37.59811650797162)
                .activity(activity2)
                .build();

        locationRepository.save(location5);

        Location location6 = Location.builder()
                .profile(profile2)
                .longitude(126.91508093028813 )
                .latitude(37.59732800321048)
                .activity(activity2)
                .build();

        locationRepository.save(location6);

        Location endlocation2 = Location.builder()
                .profile(profile2)
                .longitude(126.91504295184949)
                .latitude(37.59587287526493)
                .activity(activity2)
                .build();

        locationRepository.save(endlocation2);

        Activity activity3 = Activity.builder()
                .profile(profile3)
                .ploggingTime(120)
                .distance(5.0)
                .startPlace(geocodeService.getAddress(37.598769835423475,126.91542467032245))
                .endPlace(geocodeService.getAddress(37.59587287526493,126.91504295184949))
                .routeStatus(true)
                .ploggingDate(LocalDate.now())
                .build();
        activityRepository.save(activity3);

        Location startlocation3 = Location.builder()
                .profile(profile3)
                .longitude(126.91542467032245)
                .latitude(37.598769835423475)
                .activity(activity3)
                .build();

        locationRepository.save(startlocation3);

        Location location7 = Location.builder()
                .profile(profile3)
                .longitude(126.91534860669759)
                .latitude(37.59844091925552)
                .activity(activity3)
                .build();

        locationRepository.save(location7);

        Location location8 = Location.builder()
                .profile(profile3)
                .longitude(126.91527253863138)
                .latitude(37.59811650797162)
                .activity(activity3)
                .build();

        locationRepository.save(location8);

        Location location9 = Location.builder()
                .profile(profile3)
                .longitude(126.91508093028813 )
                .latitude(37.59732800321048)
                .activity(activity3)
                .build();

        locationRepository.save(location9);

        Location endlocation3 = Location.builder()
                .profile(profile3)
                .longitude(126.91504295184949)
                .latitude(37.59587287526493)
                .activity(activity3)
                .build();

        locationRepository.save(endlocation3);

        User user4 = User.builder()
                .userAccount("user4")
                .userPw("1234")
                .userEmail("email4")
                .userUUID(UUID.randomUUID())
                .build();
        userRepository.save(user4);

        Profile profile4 = Profile.builder()
                .userNickname("plog")
                .user(user4)
                .badge(badge)
                .totalTrash(50)
                .totalTime(50.0)
                .totalCoin(50)
                .totalDistance(50.0)
                .userMembership(false)
                .ploggingStatus(true)
                .build();
        profileRepository.save(profile4);

        Badge badge4 = Badge.builder()
                .badgeGoal("기본 배지")
                .cost(0)
                .build();
        badgeRepository.save(badge4);

        MyBadge myBadge4 = MyBadge.builder()
                .badge(badge4)
                .profile(profile4)
                .myBadgeStatus(true)
                .build();
        myBadgeRepository.save(myBadge4);

        Activity activity4 = Activity.builder()
                .profile(profile4)
                .ploggingTime(120)
                .distance(5.0)
                .startPlace(geocodeService.getAddress(37.598769835423475,126.91542467032245))
                .endPlace(geocodeService.getAddress(37.59587287526493,126.91504295184949))
                .routeStatus(true)
                .ploggingDate(LocalDate.now())
                .build();
        activityRepository.save(activity4);

        Location startlocation4 = Location.builder()
                .profile(profile4)
                .longitude(126.91542467032245)
                .latitude(37.598769835423475)
                .activity(activity4)
                .build();

        locationRepository.save(startlocation4);

        Location location10 = Location.builder()
                .profile(profile4)
                .longitude(126.91534860669759)
                .latitude(37.59844091925552)
                .activity(activity4)
                .build();

        locationRepository.save(location10);

        Location location11 = Location.builder()
                .profile(profile4)
                .longitude(126.91527253863138)
                .latitude(37.59811650797162)
                .activity(activity4)
                .build();

        locationRepository.save(location11);

        Location location12 = Location.builder()
                .profile(profile4)
                .longitude(126.91508093028813 )
                .latitude(37.59732800321048)
                .activity(activity4)
                .build();

        locationRepository.save(location12);

        Location endlocation4 = Location.builder()
                .profile(profile4)
                .longitude(126.91504295184949)
                .latitude(37.59587287526493)
                .activity(activity4)
                .build();

        locationRepository.save(endlocation4);

        Activity activity5 = Activity.builder()
                .profile(profile4)
                .ploggingTime(120)
                .distance(5.0)
                .startPlace(geocodeService.getAddress(37.598769835423475,126.91542467032245))
                .endPlace(geocodeService.getAddress(37.59587287526493,126.91504295184949))
                .routeStatus(true)
                .ploggingDate(LocalDate.now())
                .build();
        activityRepository.save(activity5);

        Location startlocation5 = Location.builder()
                .profile(profile4)
                .longitude(126.91542467032245)
                .latitude(37.598769835423475)
                .activity(activity5)
                .build();

        locationRepository.save(startlocation5);

        Location location13 = Location.builder()
                .profile(profile4)
                .longitude(126.91534860669759)
                .latitude(37.59844091925552)
                .activity(activity5)
                .build();

        locationRepository.save(location13);

        Location location14 = Location.builder()
                .profile(profile4)
                .longitude(126.91527253863138)
                .latitude(37.59811650797162)
                .activity(activity5)
                .build();

        locationRepository.save(location14);

        Location location15 = Location.builder()
                .profile(profile4)
                .longitude(126.91508093028813 )
                .latitude(37.59732800321048)
                .activity(activity5)
                .build();

        locationRepository.save(location15);

        Location endlocation5 = Location.builder()
                .profile(profile4)
                .longitude(126.91504295184949)
                .latitude(37.59587287526493)
                .activity(activity5)
                .build();

        locationRepository.save(endlocation5);

        Trash trash1 = Trash.builder()
                .garbage(10)
                .can(5)
                .plastic(3)
                .paper(2)
                .plastic_bag(1)
                .glass(0)
                .activity(activity) // 기존 Activity와 연결
                .build();
        trashRepository.save(trash1); // TrashRepository 필요

        Trash trash2 = Trash.builder()
                .garbage(20)
                .can(10)
                .plastic(5)
                .paper(4)
                .plastic_bag(2)
                .glass(1)
                .activity(activity2) // 다른 Activity와 연결
                .build();
        trashRepository.save(trash2);

        Trash trash3 = Trash.builder()
                .garbage(2)
                .can(1)
                .plastic(5)
                .paper(4)
                .plastic_bag(2)
                .glass(1)
                .activity(activity3) // 다른 Activity와 연결
                .build();
        trashRepository.save(trash3);

        Trash trash4 = Trash.builder()
                .garbage(3)
                .can(3)
                .plastic(7)
                .paper(8)
                .plastic_bag(3)
                .glass(2)
                .activity(activity4) // 다른 Activity와 연결
                .build();
        trashRepository.save(trash4);

        Trash trash5 = Trash.builder()
                .garbage(3)
                .can(3)
                .plastic(7)
                .paper(8)
                .plastic_bag(3)
                .glass(2)
                .activity(activity5) // 다른 Activity와 연결
                .build();
        trashRepository.save(trash5);

        // 더미 Rank 데이터 추가
        Rank rank1 = Rank.builder()
                .profile(profile) // 기존 Profile과 연결
                .distance(5.0)
                .trash_sum(trash1.getTrash_sum()) // Trash의 쓰레기 개수 사용
                .build();
        rankRepository.save(rank1); // RankRepository 필요

        Rank rank2 = Rank.builder()
                .profile(profile2) // 다른 Profile과 연결
                .distance(10.0)
                .trash_sum(trash2.getTrash_sum()) // Trash의 쓰레기 개수 사용
                .build();
        rankRepository.save(rank2);

        Rank rank3 = Rank.builder()
                .profile(profile3) // 다른 Profile과 연결
                .distance(2.0)
                .trash_sum(trash3.getTrash_sum()) // Trash의 쓰레기 개수 사용
                .build();
        rankRepository.save(rank3);

        Rank rank4 = Rank.builder()
                .profile(profile4) // 다른 Profile과 연결
                .distance(3.0)
                .trash_sum(trash4.getTrash_sum()) // Trash의 쓰레기 개수 사용
                .build();
        rankRepository.save(rank4);

        Rank rank5 = Rank.builder()
                .profile(profile4) // 다른 Profile과 연결
                .distance(3.0)
                .trash_sum(trash5.getTrash_sum()) // Trash의 쓰레기 개수 사용
                .build();
        rankRepository.save(rank5);

        log.info("더미 Trash 및 Rank 데이터 저장 완료");
    }
}