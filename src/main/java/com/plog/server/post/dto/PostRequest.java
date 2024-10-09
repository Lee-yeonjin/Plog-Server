package com.plog.server.post.dto;

import com.plog.server.user.domain.User;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostRequest {
    private String title;

    private String content;

    private String place;

    private LocalDateTime time;

    private String schedule;

    private User user;
}
