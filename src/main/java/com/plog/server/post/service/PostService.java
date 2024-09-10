package com.plog.server.post.service;

import com.plog.server.badge.domain.MyBadge;
import com.plog.server.badge.repository.MyBadgeRepository;
import com.plog.server.post.domain.Fcm;
import com.plog.server.post.domain.Join;
import com.plog.server.post.domain.Like;
import com.plog.server.post.domain.Post;
import com.plog.server.post.dto.FcmSend;
import com.plog.server.post.dto.LikeResponse;
import com.plog.server.post.dto.PostDetailResponse;
import com.plog.server.post.dto.PostListResponse;
import com.plog.server.post.repository.FcmRepository;
import com.plog.server.post.repository.JoinRepository;
import com.plog.server.post.repository.LikeRepository;
import com.plog.server.post.repository.PostRepository;
import com.plog.server.profile.domain.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.UUID;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final JoinRepository joinRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final MyBadgeRepository myBadgeRepository;
    private final FcmService fcmService;
    private final FcmRepository fcmRepository;

    // 게시글 아이디 찾기
    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    // 게시글 생성
    public Post createPost(Profile profile, PostDetailResponse postRequest) {
        Post post = Post.builder()
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .plogPlace(postRequest.getPlogPlace())
                .meetPlace(postRequest.getMeetPlace())
                .schedule(postRequest.getSchedule())
                .time(LocalDate.now())
                .profile(profile)
                .joinCount(0)
                .build();

        Post savedPost = postRepository.save(post);

        // 알림 전송

        List<Fcm> enabledFcmList = fcmRepository.findByNotificationEnabledTrue();
        for (Fcm fcm : enabledFcmList) {
            String targetToken = fcm.getDeviceToken(); // deviceToken을 가져옴
            FcmSend fcmSend = new FcmSend(targetToken, "새 게시글 알림", "새 게시글이 생성되었습니다: " + savedPost.getTitle());
            fcmService.sendMessageTo(fcmSend);
        }
        return savedPost;
    }

    // 게시글 삭제
    public void deletePost(Long postId) {
        Post post = findPostById(postId);
        postRepository.delete(post);
    }

    // 게시글 상세 조회
    public PostDetailResponse getPostDetail(Profile profile, Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        boolean isLiked = checkIfLiked(profile, post);
        boolean isJoined = checkIfJoined(profile, post);

        List<MyBadge> mainBadges = myBadgeRepository.findMainBadgesByProfile(post.getProfile());

        Optional<Long> badgeIdOptional = mainBadges.stream()
                .filter(myBadge -> myBadge.myBadgeStatus()) // 정확한 메서드 이름 사용
                .map(myBadge -> myBadge.getBadge().getBadgeId())
                .findFirst();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = post.getTime().format(formatter);

        int badgeId = badgeIdOptional.map(Long::intValue).orElse(1);

        return new PostDetailResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getPlogPlace(),
                post.getMeetPlace(),
                formattedDate,
                post.getSchedule(),
                post.getProfile().getUserNickname(),
                isLiked,
                isJoined,
                badgeId
        );
    }

    private boolean checkIfLiked(Profile profile, Post post) {
        return likeRepository.existsByProfileAndPost(profile, post);
    }

    private boolean checkIfJoined(Profile profile, Post post) {
        return joinRepository.existsByProfileAndPost(profile, post);
    }

    // 게시글 목록 조회
    public List<PostListResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(post -> {
                    // 작성자의 배지 ID를 조회
                    List<MyBadge> mainBadges = myBadgeRepository.findMainBadgesByProfile(post.getProfile());
                    Optional<Long> badgeIdOptional = mainBadges.stream()
                            .filter(myBadge -> myBadge.myBadgeStatus())
                            .map(myBadge -> myBadge.getBadge().getBadgeId())
                            .findFirst();

                    // 배지 ID가 없으면 기본값 1 사용
                    int badgeId = badgeIdOptional.map(Long::intValue).orElse(1);

                    return new PostListResponse(
                            post.getPostId(),
                            post.getTitle(),
                            post.getTime(), // LocalDate를 그대로 전달
                            post.getProfile().getUserNickname(),
                            badgeId // 배지 ID 추가
                    );
                })
                .collect(Collectors.toList());
    }

    // 내가 작성한 게시글 목록 조회
    public List<PostListResponse> getPostList(Profile profile) {
        // 작성자의 배지 ID를 조회
        List<MyBadge> mainBadges = myBadgeRepository.findMainBadgesByProfile(profile);

        // 활성화된 배지 ID를 가져옵니다.
        Optional<Long> badgeIdOptional = mainBadges.stream()
                .filter(MyBadge::myBadgeStatus)
                .map(myBadge -> myBadge.getBadge().getBadgeId())
                .findFirst();

        // 배지 ID가 없으면 기본값 1 사용
        int badgeId = badgeIdOptional.map(Long::intValue).orElse(1); // Long을 int로 변환

        // Profile을 기반으로 게시글 목록 조회
        return postRepository.findByProfile(profile).stream()
                .map(post -> new PostListResponse(
                        post.getPostId(),
                        post.getTitle(),
                        post.getTime(), // LocalDate를 그대로 전달
                        post.getProfile().getUserNickname(),
                        badgeId // 배지 ID 추가
                ))
                .collect(Collectors.toList());
    }

    // 게시글 참가
    public void joinPost(Profile profile, Long postId) {
        Post post = findPostById(postId);

        if (joinRepository.existsByProfileAndPost(profile, post)) {
            return;
        }

        com.plog.server.post.domain.Join join = com.plog.server.post.domain.Join.builder()
                .profile(profile)
                .post(post)
                .build();
        joinRepository.save(join);

        post.setJoinCount(post.getJoinCount() + 1);
        postRepository.save(post);
    }

    // 참가확인
    public boolean hasUserJoinedPost(Profile profile, Long postId) {
        return joinRepository.existsByProfileAndPost(profile, findPostById(postId));
    }

    // 내가 참가 신청한 게시글 목록 조회
    public List<PostListResponse> getJoinedPostsByProfile(Profile profile) {
        List<Post> joinedPosts = postRepository.findJoinedPostsByProfile(profile);

        return joinedPosts.stream()
                .map(post -> {
                    // 게시글 작성자의 배지 ID를 조회
                    List<MyBadge> authorBadges = myBadgeRepository.findMainBadgesByProfile(post.getProfile());
                    Optional<Long> authorBadgeIdOptional = authorBadges.stream()
                            .filter(MyBadge::myBadgeStatus)
                            .map(myBadge -> myBadge.getBadge().getBadgeId())
                            .findFirst();

                    // 배지 ID가 없으면 기본값 1 사용
                    int BadgeId = authorBadgeIdOptional.map(Long::intValue).orElse(1); // Long을 int로 변환

                    return new PostListResponse(
                            post.getPostId(),
                            post.getTitle(),
                            post.getTime(), // LocalDate를 그대로 전달
                            post.getProfile().getUserNickname(),
                            BadgeId // 게시글 작성자의 배지 ID 추가
                    );
                })
                .collect(Collectors.toList());
    }

    // 참가취소
    public void cancelJoinPost(Profile profile, Long postId) {
        Post post = findPostById(postId);

        Join join = joinRepository.findByProfileAndPost(profile, post)
                .orElseThrow(() -> new RuntimeException("참여하지 않은 게시글입니다."));

        joinRepository.delete(join); // Join 객체 삭제
    }

    // 참가 count 감소
    public void decreaseJoinCount(Long postId) {
        Post post = findPostById(postId);
        if (post != null) {
            post.setJoinCount(post.getJoinCount() - 1);
            postRepository.save(post);
        } else {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
    }

    // 게시글 찜
    public void likePost(Profile profile, Long postId) {
        Post post = findPostById(postId);

        Like like = Like.builder()
                .profile(profile)
                .post(post)
                .build();
        likeRepository.save(like);

        post.setLikeCount(post.getLikeCount() + 1);
        postRepository.save(post);
    }

    // 게시글 찜 중복 확인
    public boolean hasUserLikedPost(Profile profile, Long postId) {
        return likeRepository.existsByProfileAndPost(profile, findPostById(postId));
    }

    // 게시글 찜 취소
        public void unlikePost(Profile profile, Long postId) {
        Post post = findPostById(postId);

        com.plog.server.post.domain.Like like = likeRepository.findByProfileAndPost(profile, post)
                .orElseThrow(() -> new RuntimeException("찜하지 않은 게시글입니다."));

        likeRepository.delete(like);
    }

    // 찜 count 감소
    public void decreaseLikeCount(Long postId) {
        Post post = findPostById(postId);
        if (post != null) {
            post.setJoinCount(post.getLikeCount() - 1);
            postRepository.save(post);
        } else {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
    }

    // 찜한 게시글 목록 조회
    public List<LikeResponse> getLikedPostsByProfile(Profile profile) {
        List<Like> likedPosts = likeRepository.findByProfile(profile);

        return likedPosts.stream()
                .map(like -> {
                    Post post = like.getPost();

                    // 게시글 작성자의 배지 ID를 조회
                    List<MyBadge> authorBadges = myBadgeRepository.findMainBadgesByProfile(post.getProfile());
                    Optional<Long> authorBadgeIdOptional = authorBadges.stream()
                            .filter(MyBadge::myBadgeStatus)
                            .map(myBadge -> myBadge.getBadge().getBadgeId())
                            .findFirst();

                    // 배지 ID가 없으면 기본값 1 사용
                    int badgeId = authorBadgeIdOptional.map(Long::intValue).orElse(1);

                    return new LikeResponse(
                            post.getPostId(),
                            post.getTitle(),
                            post.getTime(),
                            post.getProfile().getUserNickname(),
                            badgeId // 게시글 작성자의 배지 ID 추가
                    );
                })
                .collect(Collectors.toList());
    }
}