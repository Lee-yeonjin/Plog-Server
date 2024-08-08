package com.plog.server.post.service;

import com.plog.server.post.domain.Post;
import com.plog.server.post.dto.JoinResponse;
import com.plog.server.post.dto.LikeResponse;
import com.plog.server.post.dto.PostDetailResponse;
import com.plog.server.post.dto.PostListResponse;
import com.plog.server.post.repository.JoinRepository;
import com.plog.server.post.repository.LikeRepository;
import com.plog.server.post.repository.PostRepository;
import com.plog.server.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public void joinPost(User user, Long postId) {
        Post post = findPostById(postId);

        if (joinRepository.existsByUserAndPost(user, post)) {
            throw new RuntimeException("이미 참여한 게시글입니다.");
        }

        com.plog.server.post.domain.Join join = com.plog.server.post.domain.Join.builder()
                .user(user)
                .post(post)
                .build();
        joinRepository.save(join);
    }

    public void cancelJoinPost(User user, Long postId) {
        Post post = findPostById(postId);

        com.plog.server.post.domain.Join join = joinRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new RuntimeException("참여하지 않은 게시글입니다."));

        joinRepository.delete(join); // Join 객체 삭제
    }

    public void increaseJoinCount(Long postId) {
        Post post = findPostById(postId);
        if (post != null) {
            post.setJoinCount(post.getJoinCount() + 1);
            postRepository.save(post);
        } else {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
    }


    public void decreaseJoinCount(Long postId) {
        Post post = findPostById(postId);
        if (post != null) {
            post.setJoinCount(post.getJoinCount() - 1);
            postRepository.save(post);
        } else {
            throw new IllegalArgumentException("존재하지 않는 게시글입니다.");
        }
    }

    public boolean hasUserLikedPost(User user, Long postId) {
        return likeRepository.existsByUserAndPost(user, findPostById(postId));
    }

    public boolean hasUserJoinedPost(User user, Long postId) {
        return joinRepository.existsByUserAndPost(user, findPostById(postId));
    }

    public List<JoinResponse> getJoinedPostsByUser(User user) {
        List<com.plog.server.post.domain.Join> joinedPosts = joinRepository.findByUser(user);

        return joinedPosts.stream()
                .map(join -> {
                    Post post = join.getPost();
                    return new JoinResponse(
                            post.getPostId(),
                            post.getTitle(),
                            post.getTime(),
                            post.getUser().getUserNickname()
                    );
                })
                .collect(Collectors.toList());
    }

    public void likePost(User user, Long postId) {
        Post post = findPostById(postId);

        com.plog.server.post.domain.Like like = com.plog.server.post.domain.Like.builder()
                .user(user)
                .post(post)
                .build();
        likeRepository.save(like);
    }

    public void unlikePost(User user, Long postId) {
        Post post = findPostById(postId);

        com.plog.server.post.domain.Like like = likeRepository.findByUserAndPost(user, post)
                .orElseThrow(() -> new RuntimeException("찜하지 않은 게시글입니다."));

        likeRepository.delete(like);
    }

    public List<LikeResponse> getLikedPostsByUser(User user) {
        List<com.plog.server.post.domain.Like> likedPosts = likeRepository.findByUser(user);

        return likedPosts.stream()
                .map(like -> {
                    Post post = like.getPost();
                    return new LikeResponse(
                            post.getPostId(),
                            post.getTitle(),
                            post.getTime(),
                            post.getUser().getUserNickname()
                    );
                })
                .collect(Collectors.toList());
    }


    public Post createPost(Post post) {

        return postRepository.save(post);
    }

    public void deletePost(Long postId) {
        Post post = findPostById(postId);
        postRepository.delete(post);
    }

    public PostDetailResponse getPostDetail(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        return new PostDetailResponse(
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getPlace(),
                post.getTime(),
                post.getSchedule(),
                post.getUser().getUserNickname()
        );
    }

    public List<PostListResponse> getAllPosts() {
        return postRepository.findAll().stream()
                .map(post -> new PostListResponse(
                        post.getPostId(),
                        post.getTitle(),
                        post.getTime(),
                        post.getUser().getUserNickname()))
                .collect(Collectors.toList());
    }
}
