package com.plog.server.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeRequest {
    private boolean notificationEnabled; // 알림 설정 여부
    private String Location;
}
