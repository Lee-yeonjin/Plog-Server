package com.plog.server.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ActiveProfileResponse {
    private String userNickname;
    private Long badgeId;
}
