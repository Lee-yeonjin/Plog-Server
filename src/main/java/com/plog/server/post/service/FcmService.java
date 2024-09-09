
package com.plog.server.post.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.plog.server.post.domain.Fcm;
import com.plog.server.post.dto.FcmMessage;
import com.plog.server.post.dto.FcmSend;
import com.plog.server.post.dto.NoticeResponse;
import com.plog.server.post.repository.FcmRepository;
import com.plog.server.profile.domain.Profile;
import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
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
    private final String API_URL = "https://fcm.googleapis.com/v1/projects/716528667886/messages:send";
    private final ObjectMapper objectMapper;

    // 알림설정 여부 true인 사람들 찾아내기
    public List<Long> getEnabledProfileIds() {
        List<Fcm> enabledFcmList = fcmRepository.findByNotificationEnabledTrue();
        return enabledFcmList.stream()
                .map(fcm -> fcm.getProfile().getProfileId()) // ProfileId가 있는 경우
                .collect(Collectors.toList());
    }

    // Fcm 테이블 알림설정
    public void saveOrUpdateFcm(Profile profile, NoticeResponse noticeResponse) {
        Optional<Fcm> existingFcm = fcmRepository.findByProfile(profile);

        Fcm fcm;
        if (existingFcm.isPresent()) {
            // 기존 Fcm 업데이트
            fcm = existingFcm.get();
            fcm.setNotificationEnabled(noticeResponse.isNotificationEnabled());
            fcm.setLatitude(noticeResponse.getLatitude());
            fcm.setLongitude(noticeResponse.getLongitude());
            fcm.setDeviceToken(noticeResponse.getDeviceToken());
        } else {
            // 새로운 Fcm 생성
            fcm = Fcm.builder()
                    .profile(profile)
                    .notificationEnabled(noticeResponse.isNotificationEnabled())
                    .latitude(noticeResponse.getLatitude())
                    .longitude(noticeResponse.getLongitude())
                    .deviceToken(noticeResponse.getDeviceToken())
                    .build();
        }

        // 저장
        fcmRepository.save(fcm);
    }

    // 푸시알림 전송
    public void sendMessageTo(FcmSend fcmSend) throws IOException {
        String message = makeMessage(fcmSend);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(
                message.getBytes(StandardCharsets.UTF_8),
                MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();
        if (response.body() != null) {
            System.out.println(response.body().string());
        } else {
            System.out.println("Response body is null");
        }

//        try {
//            String message = makeMessage(fcmSend);
//
//            OkHttpClient client = new OkHttpClient(); // 재사용을 고려
//            RequestBody requestBody = RequestBody.create(
//                    message.getBytes(StandardCharsets.UTF_8),
//                    MediaType.get("application/json; charset=utf-8")
//            );
//            Request request = new Request.Builder()
//                    .url(API_URL)
//                    .post(requestBody)
//                    .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
//                    .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8")
//                    .build();
//
//            Response response = client.newCall(request).execute();
//            if (response.isSuccessful()) {
//                System.out.println(response.body().string());
//            } else {
//                System.out.println("Error: " + response.code() + " - " + response.message());
//            }
//        } catch (IOException e) {
//            e.printStackTrace(); // 예외 로그 출력
//        }
    }

    public void sendNotificationsToEnabledUsers(String title, String body) throws IOException {
        List<Fcm> enabledFcmList = fcmRepository.findByNotificationEnabledTrue();

        for (Fcm fcm : enabledFcmList) {
            FcmSend fcmSend = FcmSend.builder()
                    .targetToken(fcm.getDeviceToken())
                    .title(title)
                    .body(body)
                    .build();
            sendMessageTo(fcmSend);
        }
    }

    private String makeMessage(FcmSend fcmSend) throws JsonParseException, JsonProcessingException {
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
        return objectMapper.writeValueAsString(fcmMessage);
    }

    private String getAccessToken() throws IOException {
        String firebaseConfigPath = "firebase/plog-97f27-firebase-adminsdk-o0hru-b2cd8f2612";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}