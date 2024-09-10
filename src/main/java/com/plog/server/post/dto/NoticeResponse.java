package com.plog.server.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeResponse {
    private boolean notificationEnabled; // 알림 설정 여부
    private Double latitude; // 알림 위도
    private Double longitude; // 알림 경도
    private String deviceToken; // 디바이스 토큰
}
