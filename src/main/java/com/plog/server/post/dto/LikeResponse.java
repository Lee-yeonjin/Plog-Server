package com.plog.server.post.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class LikeResponse {
    private Long postId;
    private String title;
    private String  time;
    private String userNickname;

    public LikeResponse(Long postId, String title, LocalDate time, String userNickname) {
        this.postId = postId;
        this.title = title;
        this.time = time.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.userNickname = userNickname;
    }
}

