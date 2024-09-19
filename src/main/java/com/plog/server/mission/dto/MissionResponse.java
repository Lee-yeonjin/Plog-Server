package com.plog.server.mission.dto;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MissionResponse {
    private String message;
    private int missionId;
    private int coinCount; // 코인 개수 추가
    private boolean isFinish;
}
