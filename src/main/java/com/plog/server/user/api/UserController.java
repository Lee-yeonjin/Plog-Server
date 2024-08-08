package com.plog.server.user.api;

import com.plog.server.global.ApiResponse;
import com.plog.server.user.domain.EmailToken;
import com.plog.server.user.domain.User;
import com.plog.server.user.domain.UserTemp;
import com.plog.server.user.dto.LoginRequestDto;
import com.plog.server.user.dto.SignUpFinishRequest;
import com.plog.server.user.dto.SignUpRequest;
import com.plog.server.user.dto.UserResponseDto;
import com.plog.server.user.repository.EmailTokenRepository;
import com.plog.server.user.repository.UserTempRepository;
import com.plog.server.user.service.EmailService;
import com.plog.server.user.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;
    private final EmailService emailService;

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequestDto loginRequest, HttpSession session) {
        try {
            UserResponseDto userResponseDto = userService.login(loginRequest);

            session.setAttribute("user", userResponseDto);

            ApiResponse apiResponse = new ApiResponse(userResponseDto.getUserUUID().toString(), userResponseDto.getUserNickname());

            Map<String, Object> responseData = new HashMap<>();
            responseData.put("userUUID", userResponseDto.getUserUUID().toString());
            responseData.put("userNickname", userResponseDto.getUserNickname());

            return ResponseEntity.ok(new ApiResponse("로그인 성공", responseData));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("서버 오류: " + e.getMessage()));
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<ApiResponse> logout(HttpSession session) {
        // 세션 무효화
        session.invalidate();
        ApiResponse apiResponse = new ApiResponse("로그아웃 되었습니다.", null);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/userid")
    public ResponseEntity<User> getUserByUUID(@RequestParam Long userId) {
        Optional<User> user = userService.getUserByUUID(userId);

        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("signup")
    public ResponseEntity<ApiResponse> signUp(@RequestBody SignUpRequest signUpRequest) throws MessagingException {
        UserTemp userTemp = userService.signUpUserTemp(signUpRequest);
        Map<String, Object> response = emailService.createEmailToken(userTemp);
        ApiResponse apiResponse = new ApiResponse("임시 회원 가입 완료", response);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    //회원가입 완료
    @PostMapping("/sign-up-finish")
    public ResponseEntity<ApiResponse> signUpFinish(@RequestBody SignUpFinishRequest signUpFinishRequest) {
        return ResponseEntity.ok(new ApiResponse(userService.signUpFinish(signUpFinishRequest.getAccount()), HttpStatus.OK));
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<ApiResponse> confrimEmail(@RequestParam String uuid){
        log.info("받은 UUID: {}", uuid);

        UserTemp userTemp = emailService.findByUuid(uuid);

        log.info("찾은 UserTemp: {}", userTemp);

        userService.signUpUser(userTemp);
        return ResponseEntity.ok(new ApiResponse("인증 성공"));
    }
}