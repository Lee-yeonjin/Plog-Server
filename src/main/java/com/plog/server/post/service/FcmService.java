package com.plog.server.post.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.google.firebase.messaging.*;
import com.plog.server.post.domain.Fcm;
import com.plog.server.post.dto.FcmMessage;
import com.plog.server.post.dto.FcmSend;
import com.plog.server.post.dto.NoticeRequest;
import com.plog.server.post.dto.NoticeResponse;
import com.plog.server.post.repository.FcmRepository;
import com.plog.server.profile.domain.Profile;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FcmService {

    private final FcmRepository fcmRepository;
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/plog-97f27/messages:send";
    private final ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper 인스턴스 생성

    // Mypage 들어올 때 알림설정 여부 & 장소 반환
    public NoticeRequest getNoticeRequestByProfileId(Profile profile) {
        Fcm fcm = fcmRepository.findByProfile(profile)
                .orElseThrow(() -> new IllegalArgumentException("FCM 정보가 없습니다."));

        NoticeRequest noticeRequest = new NoticeRequest();
        noticeRequest.setNotificationEnabled(fcm.isNotificationEnabled());
        noticeRequest.setLocation(fcm.getLocation() != null ? fcm.getLocation() : "장소없음");

        return noticeRequest;
    }

    // 로그인 할 때 device 토큰 값 받아서 update
    public void saveOrUpdateFcm(Profile profile, String deviceToken) {
        Optional<Fcm> existingFcm = fcmRepository.findByProfile(profile);

        Fcm fcm;
        if (existingFcm.isPresent()) {
            fcm = existingFcm.get();
            // deviceToken만 업데이트
            fcm.setDeviceToken(deviceToken);
        } else {
            // 새로운 Fcm 객체 생성
            fcm = Fcm.builder()
                    .profile(profile)
                    .deviceToken(deviceToken)
                    .build();
        }
        fcmRepository.save(fcm);
    }

    // Fcm 테이블 데이터 생성 & 수정
    public void saveOrUpdateFcm(Profile profile, NoticeResponse noticeResponse) {
        Optional<Fcm> existingFcm = fcmRepository.findByProfile(profile);

        Fcm fcm;
        if (existingFcm.isPresent()) {
            fcm = existingFcm.get();
            fcm.setNotificationEnabled(noticeResponse.isNotificationEnabled());
            fcm.setLatitude(noticeResponse.getLatitude());
            fcm.setLongitude(noticeResponse.getLongitude());
            fcm.setLocation(noticeResponse.getLocation());
        } else {
            fcm = Fcm.builder()
                    .profile(profile)
                    .notificationEnabled(noticeResponse.isNotificationEnabled())
                    .latitude(noticeResponse.getLatitude())
                    .longitude(noticeResponse.getLongitude())
                    .location(noticeResponse.getLocation())
                    .build();
        }
        fcmRepository.save(fcm);
    }

    // 푸시알림 전송
    public void sendMessageTo(FcmSend fcmSend) {
        String message = makeMessage(fcmSend);
        System.out.println("Sending message:" + message);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(
                message.getBytes(StandardCharsets.UTF_8),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                System.out.println(response.body().string());
            } else {
                System.out.println("Response body is null");
            }
        } catch (IOException e) {
            e.printStackTrace(); // 예외 로그 출력
        }
    }

    private String makeMessage(FcmSend fcmSend) {
        try {
            FcmMessage fcmMessage = FcmMessage.builder()
                    .message(FcmMessage.Message.builder()
                            .token(fcmSend.getTargetToken())
                            .notification(FcmMessage.Notification.builder()
                                    .title(fcmSend.getTitle())
                                    .body(fcmSend.getBody())
                                    .build())
                            .build())
                    .validateOnly(false)
                    .build();
            String jsonMessage = objectMapper.writeValueAsString(fcmMessage);
            System.out.println("Constructed FCM message: " + jsonMessage); // 로그 추가
            return jsonMessage;
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // 예외 로그 출력
            return null; // 또는 적절한 기본값 처리
        }
    }

    private String getAccessToken() {
        String firebaseConfigPath = "firebase/plog-97f27-firebase-adminsdk-o0hru-b2cd8f2612.json";

        System.out.println("Attempting to load file from: " + firebaseConfigPath);

        try {
            GoogleCredentials googleCredentials = GoogleCredentials
                    .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                    .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

            googleCredentials.refreshIfExpired();
            return googleCredentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            e.printStackTrace(); // 예외 로그 출력
            return null; // 또는 적절한 기본값 처리
        }
    }
}