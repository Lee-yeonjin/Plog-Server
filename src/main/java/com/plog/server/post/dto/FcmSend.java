package com.plog.server.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmSend {
    private String targetToken;
    private String title;
    private String body;
}