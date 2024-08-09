package com.plog.server.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostListResponse {
    private Long postId;
    private String title;
    private String time;
    private String userNickname;

    public PostListResponse(Long postId, String title, LocalDate time, String userNickname) {
        this.postId = postId;
        this.title = title;
        this.time = time.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")); // 포맷팅
        this.userNickname = userNickname;
    }
}