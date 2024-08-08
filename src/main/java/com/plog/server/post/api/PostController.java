package com.plog.server.post.api;

import com.plog.server.global.ApiResponse;
import com.plog.server.post.domain.Post;
import com.plog.server.post.dto.JoinResponse;
import com.plog.server.post.dto.LikeResponse;
import com.plog.server.post.dto.PostDetailResponse;
import com.plog.server.post.dto.PostListResponse;
import com.plog.server.post.service.PostService;
import com.plog.server.user.domain.User;
import com.plog.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final UserService userService;

    // 게시글 생성
    @PostMapping("/{uuid}/createpost")
    public ResponseEntity<ApiResponse> createPost(@PathVariable UUID uuid, @RequestBody PostDetailResponse postRequest) {
        User user = userService.getUserByUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = Post.builder() // Post 객체를 생성
                .title(postRequest.getTitle())
                .content(postRequest.getContent())
                .place(postRequest.getPlace())
                .schedule(postRequest.getSchedule())
                .time(postRequest.getTime())
                .user(user)
                .joinCount(0)
                .build();

        postService.createPost(post);
        ApiResponse apiResponse = new ApiResponse("게시글 생성", null);
        return ResponseEntity.ok(apiResponse);
    }

    // 게시글 삭제
    @PostMapping("/{uuid}/{postId}/deletepost")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable UUID uuid, @PathVariable Long postid) {
        User user = userService.getUserByUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = postService.findPostById(postid);
        if (post == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("존재하지 않는 게시글입니다", null));
        }

        if (!post.getUser().getUserUUID().equals(user.getUserUUID())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse("게시글 삭제 권한이 없습니다", null));
        }

        postService.deletePost(postid);
        ApiResponse apiResponse = new ApiResponse("게시글 삭제", null);
        return ResponseEntity.ok(apiResponse);
    }

    // 게시글 조회 [목록]
    @GetMapping("/{uuid}/all")
    public ResponseEntity<ApiResponse> checkPost(@PathVariable UUID uuid) {
        User user = userService.getUserByUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<PostListResponse> posts = postService.getAllPosts();

        ApiResponse apiResponse = new ApiResponse("모든 게시글 조회 성공", posts);
        return ResponseEntity.ok(apiResponse);
    }

    // 게시글 조회[디테일]
    @GetMapping("/{uuid}/{postid}/post_detail")
    public ResponseEntity<ApiResponse> detailPost(@PathVariable UUID uuid, @PathVariable Long postid) {
        User user = userService.getUserByUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        PostDetailResponse postDetail = postService.getPostDetail(postid);

        ApiResponse apiResponse = new ApiResponse("게시글 상세 조회 성공", postDetail);
        return ResponseEntity.ok(apiResponse);
    }

    //참가
    @PostMapping("/{uuid}/{postid}/join")
    public ResponseEntity<ApiResponse> joinPost(@PathVariable UUID uuid, @PathVariable Long postid){
        User user = userService.getUserByUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (postService.hasUserJoinedPost(user, postid)) {
            return ResponseEntity.badRequest().body(new ApiResponse("이미 참여한 게시글입니다.", null));
        }

        postService.joinPost(user, postid);
        postService.increaseJoinCount(postid);

        ApiResponse apiResponse = new ApiResponse("참여 성공", null);
        return ResponseEntity.ok(apiResponse);
    }

    //참가취소
    @PostMapping("/{uuid}/{postid}/canceljoin")
    public ResponseEntity<ApiResponse> cancelJoin(@PathVariable UUID uuid, @PathVariable Long postid) {
        User user = userService.getUserByUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        postService.cancelJoinPost(user, postid);
        postService.decreaseJoinCount(postid);

        ApiResponse apiResponse = new ApiResponse("참여 취소", null);
        return ResponseEntity.ok(apiResponse);
    }

    // 참가 조회
    @GetMapping("/{uuid}/joincheck")
    public ResponseEntity<ApiResponse> joinCheck(@PathVariable UUID uuid) {
        User user = userService.getUserByUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<JoinResponse> joinedPosts = postService.getJoinedPostsByUser(user);

        ApiResponse apiResponse = new ApiResponse("참여한 게시글 목록", joinedPosts);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/{uuid}/{postId}/like")
    public ResponseEntity<ApiResponse> likePost(@PathVariable UUID uuid, @PathVariable Long postId) {
        User user = userService.getUserByUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (postService.hasUserJoinedPost(user, postId)) {
            return ResponseEntity.badRequest().body(new ApiResponse("이미 참가한 게시글입니다.", null));
        }

        if (postService.hasUserLikedPost(user, postId)) {
            return ResponseEntity.badRequest().body(new ApiResponse("이미 찜한 게시글입니다.", null));
        }

        postService.likePost(user, postId);
        ApiResponse apiResponse = new ApiResponse("찜 성공", null);
        return ResponseEntity.ok(apiResponse);
    }

    @PostMapping("/{uuid}/{postId}/unlike")
    public ResponseEntity<ApiResponse> unlikePost(@PathVariable UUID uuid, @PathVariable Long postId) {
        User user = userService.getUserByUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        postService.unlikePost(user, postId);
        ApiResponse apiResponse = new ApiResponse("찜 취소", null);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{uuid}/likecheck")
    public ResponseEntity<ApiResponse> likeCheck(@PathVariable UUID uuid) {
        User user = userService.getUserByUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        List<LikeResponse> likedPosts = postService.getLikedPostsByUser(user);

        ApiResponse apiResponse = new ApiResponse("찜한 게시글 목록", likedPosts);
        return ResponseEntity.ok(apiResponse);
    }
}