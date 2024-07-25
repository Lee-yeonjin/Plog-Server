package com.plog.server.user.api;

import com.plog.server.global.ApiResponse;
import com.plog.server.user.domain.User;
import com.plog.server.user.domain.UserTemp;
import com.plog.server.user.dto.SignUpRequest;
import com.plog.server.user.repository.UserRepository;
import com.plog.server.user.service.EmailService;
import com.plog.server.user.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final EmailService emailService;

    @GetMapping("/userId")
    public ResponseEntity<User> getUserByUUID(@RequestParam Long userId) {
        Optional<User> user = userService.getUserByUUID(userId);

        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("sign-up")
    public  ResponseEntity<ApiResponse> signUp(@RequestBody SignUpRequest signUpRequest) throws MessagingException {
        UserTemp userTemp = userService.signUpUserTemp(signUpRequest);
        Map<String, Object> response = emailService.createEmailToken(userTemp);
        ApiResponse apiResponse = new ApiResponse("임시 회원 가입 완료", response);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/confirm-email")
    public ResponseEntity<ApiResponse> confrimEmail(@RequestParam UUID uuid){
        UserTemp userTemp = emailService.findByUuid(uuid);
        userService.signUpUser(userTemp);

        return ResponseEntity.ok(new ApiResponse(("인증 성공")));
    }
}
