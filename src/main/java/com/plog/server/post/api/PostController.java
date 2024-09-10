package com.plog.server.post.api;

import com.plog.server.global.ApiResponse;
import com.plog.server.post.domain.Post;
import com.plog.server.post.dto.LikeResponse;
import com.plog.server.post.dto.PostDetailResponse;
import com.plog.server.post.dto.PostListResponse;
import com.plog.server.post.service.PostService;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.repository.ProfileRepository;
import com.plog.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final ProfileRepository profileRepository;
    private final UserService userService;

    // 게시글 생성
    @PostMapping("/{uuid}/createpost")
    public ResponseEntity<ApiResponse> createPost(@PathVariable UUID uuid, @RequestBody PostDetailResponse postRequest) {

        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        Post savedPost = postService.createPost(profile, postRequest);

        ApiResponse apiResponse = new ApiResponse("게시글 생성", null);
        return ResponseEntity.ok(apiResponse);
    }

    // 게시글 삭제
    @DeleteMapping("/{uuid}/{postid}/deletepost")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable UUID uuid, @PathVariable Long postid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        Post post = postService.findPostById(postid);
        if (post == null) {
            return ResponseEntity.badRequest().body(new ApiResponse("존재하지 않는 게시글입니다", null));
        }

        if (!post.getProfile().getUser().getUserUUID().equals(profile.getUser().getUserUUID())) {
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
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        List<PostListResponse> posts = postService.getAllPosts();

        ApiResponse apiResponse = new ApiResponse("모든 게시글 조회 성공", posts);
        return ResponseEntity.ok(apiResponse);
    }

    // 게시글 상세조회
    @GetMapping("/{uuid}/{postid}/post_detail")
    public ResponseEntity<ApiResponse> detailPost(@PathVariable UUID uuid, @PathVariable Long postid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        PostDetailResponse postDetail = postService.getPostDetail(profile, postid);

        ApiResponse apiResponse = new ApiResponse("게시글 상세 조회 성공", postDetail);
        return ResponseEntity.ok(apiResponse);
    }

    // 내가 작성한 게시글 목록 조회
    @GetMapping("/{uuid}/post_list")
    public ResponseEntity<ApiResponse> postList(@PathVariable UUID uuid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        List<PostListResponse> postlist = postService.getPostList(profile);

        ApiResponse apiResponse = new ApiResponse("게시글 목록 조회 성공", postlist);
        return ResponseEntity.ok(apiResponse);
    }

    //참가
    @PostMapping("/{uuid}/{postid}/join")
    public ResponseEntity<ApiResponse> joinPost(@PathVariable UUID uuid, @PathVariable Long postid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        Post post = postService.findPostById(postid);

        // 자신이 작성한 게시글인지 확인
        if (post.getProfile().getUser().getUserUUID().equals(profile.getUser().getUserUUID())) {
            return ResponseEntity.badRequest().body(new ApiResponse("자신의 게시글에는 참가할 수 없습니다.", null));
        }

        // 이미 참가한 게시글인지 확인
        if (postService.hasUserJoinedPost(profile, postid)) {
            return ResponseEntity.badRequest().body(new ApiResponse("이미 참여한 게시글입니다.", null));
        }

        // 게시글 참가 처리
        postService.joinPost(profile, postid);
        ApiResponse apiResponse = new ApiResponse("참여 성공", null);
        return ResponseEntity.ok(apiResponse);
    }

    // 참가 신청 목록조회
    @GetMapping("/{uuid}/joincheck")
    public ResponseEntity<ApiResponse> joinCheck(@PathVariable UUID uuid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        List<PostListResponse> joinedPosts = postService.getJoinedPostsByProfile(profile);

        ApiResponse apiResponse = new ApiResponse("참여한 게시글 목록", joinedPosts);
        return ResponseEntity.ok(apiResponse);
    }

    //참가취소
    @DeleteMapping("/{uuid}/{postid}/canceljoin")
    public ResponseEntity<ApiResponse> cancelJoin(@PathVariable UUID uuid, @PathVariable Long postid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        postService.cancelJoinPost(profile, postid);
        postService.decreaseJoinCount(postid);

        ApiResponse apiResponse = new ApiResponse("참여 취소", null);
        return ResponseEntity.ok(apiResponse);
    }

    // 게시글 찜
    @PostMapping("/{uuid}/{postid}/like")
    public ResponseEntity<ApiResponse> likePost(@PathVariable UUID uuid, @PathVariable Long postid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        Post post = postService.findPostById(postid);

        if (post.getProfile().getUser().getUserUUID().equals(profile.getUser().getUserUUID())) {
            return ResponseEntity.badRequest().body(new ApiResponse("자신의 게시글에는 찜할 수 없습니다.", null));
        }

        if (postService.hasUserLikedPost(profile, postid)) {
            return ResponseEntity.badRequest().body(new ApiResponse("이미 찜한 게시글입니다.", null));
        }

        postService.likePost(profile, postid);
        ApiResponse apiResponse = new ApiResponse("찜 성공", null);
        return ResponseEntity.ok(apiResponse);
    }

    // 게시글 찜 취소
    @DeleteMapping("/{uuid}/{postid}/unlike")
    public ResponseEntity<ApiResponse> unlikePost(@PathVariable UUID uuid, @PathVariable Long postid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        postService.unlikePost(profile, postid);
        postService.decreaseLikeCount(postid); // 메서드 이름 수정

        ApiResponse apiResponse = new ApiResponse("찜 취소", null);
        return ResponseEntity.ok(apiResponse);
    }

    // 게시글 찜 확인
    @GetMapping("/{uuid}/likecheck")
    public ResponseEntity<ApiResponse> likeCheck(@PathVariable UUID uuid) {
        Profile profile = profileRepository.findByUserUserUUID(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Profile not found"));

        List<LikeResponse> likedPosts =  postService.getLikedPostsByProfile(profile);

        ApiResponse apiResponse = new ApiResponse("찜한 게시글 목록", likedPosts);
        return ResponseEntity.ok(apiResponse);
    }
}