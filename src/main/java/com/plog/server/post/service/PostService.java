package com.plog.server.post.service;

import com.plog.server.post.domain.Join;
import com.plog.server.post.domain.Like;
import com.plog.server.post.domain.Post;
import com.plog.server.post.dto.LikeResponse;
import com.plog.server.post.dto.PostDetailResponse;
import com.plog.server.post.dto.PostListResponse;
import com.plog.server.post.repository.JoinRepository;
import com.plog.server.post.repository.LikeRepository;
import com.plog.server.post.repository.PostRepository;
import com.plog.server.profile.domain.Profile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 게시글 아이디 찾기
    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    // 게시글 생성
    public Post createPost(Post post) {

        return postRepository.save(post);
    }

    // 게시글 삭제
    public void deletePost(Long postId) {
        Post post = findPostById(postId);
        postRepository.delete(post);
    }

    // 게시글 상세 조회
    public PostDetailResponse getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return new PostDetailResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getPlogPlace(),
                post.getMeetPlace(),
                post.getTime(), // LocalDate를 그대로 전달
                post.getSchedule(),
                post.getProfile().getUserNickname() // 마지막 괄호 위치 수정
        ); // 괄호 위치 수정
    }

    // 게시글 목록 조회
    public List<PostListResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(post -> new PostListResponse(
                        post.getPostId(),
                        post.getTitle(),
                        post.getTime(), // LocalDate를 그대로 전달
                        post.getProfile().getUserNickname()))
                .collect(Collectors.toList());
    }

    // 내가 작성한 게시글 목록 조회
    public List<PostListResponse> getPostList(UUID userUUID) {
        return postRepository.findByProfile_UserUserUUID(userUUID).stream()
                .map(post -> new PostListResponse(
                        post.getPostId(),
                        post.getTitle(),
                        post.getTime(), // LocalDate를 그대로 전달
                        post.getProfile().getUserNickname()))
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
        List<Join> joinedPosts = joinRepository.findByProfile(profile);
        return joinedPosts.stream()
                .map(join -> {
                    Post post = join.getPost(); // Join 엔티티에서 Post 가져오기
                    return new PostListResponse(
                            post.getPostId(),
                            post.getTitle(),
                            post.getTime(),
                            post.getProfile().getUserNickname());
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
                    return new LikeResponse(
                            post.getPostId(),
                            post.getTitle(),
                            post.getTime(),
                            post.getProfile().getUserNickname()
                    );
                })
                .collect(Collectors.toList());
    }

}