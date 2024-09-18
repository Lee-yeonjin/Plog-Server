package com.plog.server.mission.dto;

import com.plog.server.mission.domain.Mission;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MissionResponse {
    private String message;
    private List<Mission> missions;
    private int coinCount; // 코인 개수 추가
}
