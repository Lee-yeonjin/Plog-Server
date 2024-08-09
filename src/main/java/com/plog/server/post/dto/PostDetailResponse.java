package com.plog.server.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponse {

    private Long postId;
    private String title;
    private String content;
    private String place;
    private String time;
    private String schedule;
    private String userNickname;

    public PostDetailResponse(Long postId, String title, String content, String place, LocalDate time, String schedule, String userNickname) {
        this.postId = postId;
        this.title = title;
        this.content = content;
        this.place = place;
        this.time = time.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")); // 포맷팅
        this.schedule = schedule;
        this.userNickname = userNickname;
    }
}
