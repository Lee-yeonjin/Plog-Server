package com.plog.server.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfileResponse {
    private String userNickname;
    private Integer totalCoin;
    private Integer badge;
    private Double totalDistance;
    private Integer  totalTime;
    private Integer totalTrash;
}
