package com.plog.server.post.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.common.net.HttpHeaders;
import com.plog.server.post.dto.FcmMessage;
import com.plog.server.post.dto.FcmSend;
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

@Component
@RequiredArgsConstructor
public class FcmService {

    private final String API_URL = "https://fcm.googleapis.com/v1/projects/plog-97f27/messages:send";
    private final ObjectMapper objectMapper;

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
        String firebaseConfigPath = "firebase/plog-97f27-firebase-adminsdk-o0hru-10335d563f.json";

        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseConfigPath).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}
