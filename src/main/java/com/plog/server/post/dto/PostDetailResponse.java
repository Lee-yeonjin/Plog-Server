package com.plog.server.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponse {

    private Long postId;
    private String title;
    private String content;
    private String plogPlace;
    private String meetPlace;
    private String time;
    private String schedule;
    private String userNickname;
    private boolean isLiked;
    private boolean isJoined;
    private Integer badgeIds;

    private double ploggingLatitude;

    private double ploggingLongitude;

    public PostDetailResponse(Long postId, String title, String content, String plogPlace, String meetPlace, String time, String schedule, String userNickname, boolean isLiked, boolean isJoined, Integer badgeIds) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.plogPlace = plogPlace;
        this.meetPlace = meetPlace;
        this.time = time; // 포맷팅
        this.schedule = schedule;
        this.userNickname = userNickname;
        this.isLiked = isLiked;
        this.isJoined = isJoined;
        this.badgeIds = badgeIds;
    }
}
