package com.plog.server.badge.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class BadgeResponse {
    private int badgeId;
    private String badgeGoal;
    private Integer cost;
}
