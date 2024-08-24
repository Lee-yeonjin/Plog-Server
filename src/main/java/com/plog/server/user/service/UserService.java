package com.plog.server.user.service;

import com.plog.server.badge.domain.Badge;
import com.plog.server.badge.repository.BadgeRepository;
import com.plog.server.profile.domain.Profile;
import com.plog.server.profile.repository.ProfileRepository;
import com.plog.server.user.domain.User;
import com.plog.server.user.domain.UserTemp;
import com.plog.server.user.dto.SignUpRequest;
import com.plog.server.user.dto.UserResponseDto;
import com.plog.server.user.repository.UserRepository;
import com.plog.server.user.repository.UserTempRepository;
import com.plog.server.user.dto.LoginRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserTempRepository userTempRepository;
    private final ProfileRepository profileRepository;
    private final BadgeRepository badgeRepository;

    //UUID 조회 추가
    public Optional<User> getUserByUUID(UUID useruuid) {
        return userRepository.findByUserUUID(useruuid);
    }

    // 로그인
    public UserResponseDto login(LoginRequestDto loginRequestDto) {
        String userAccount = loginRequestDto.getUserAccount();
        String userPw = loginRequestDto.getUserPw();

        User user = userRepository.findByUserAccount(userAccount);
        if (user == null || !user.getUserPw().equals(userPw)) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        log.info("로그인 성공 : {}", userAccount);

        return new UserResponseDto(user.getUserUUID(), user.getUserNickname());

    }

    //임시 회원 가입
    public UserTemp signUpUserTemp(SignUpRequest request){
        UserTemp userTemp = UserTemp.builder()
                .tempAccount(request.getAccount())
                .tempPw(request.getPassword())
                .tempEmail(request.getEmail())
                .tempNickname(request.getNickname())
                .tempEmailStatus(request.getIsEmailVerifien())
                .build();

        userTempRepository.save(userTemp);
        log.info("임시 회원가입 완료: {}", userTemp.getTempAccount());

        return userTempRepository.findByTempEmail(userTemp.getTempEmail());
    }

    public Optional<User> getUserByUUID(Long userid) {
        return userRepository.findByUserId(userid);
    }

    // 이메일 인증 확인 후 회원가입 완료
    public String signUpFinish(String account) {

        User user = userRepository.findByUserAccount(account);
        if (user == null) {
            throw new IllegalArgumentException("사용자가 존재하지 않습니다.");
        }

        log.info("이메일 인증된 회원: {}", account);

        return "true";
    }

    public Optional<User> getUserByUuid(UUID uuid){
        return  userRepository.findByUserUUID(uuid);
    }

    @Transactional
    public boolean signUpUser(UserTemp userTemp){
        User user = User.builder()
                .userUUID(UUID.randomUUID())
                .userAccount(userTemp.getTempAccount())
                .userPw(userTemp.getTempPw())
                .userEmail(userTemp.getTempEmail())
                .build();
        userRepository.save(user);

        Badge defaultBadge = badgeRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Default badge not found"));

        Profile profile = Profile.builder()
                .userNickname(userTemp.getTempNickname())
                .totalCoin(0)
                .totalDistance(0.0)
                .totalTime(0.0)
                .totalTrash(0)
                .badge(defaultBadge)
                .build();
        profileRepository.save(profile);

        userTempRepository.delete(userTemp);
        userTempRepository.flush();
        log.info("임시 회원 삭제 완료: {}", user.getUserAccount());

        return true;
    }
}



