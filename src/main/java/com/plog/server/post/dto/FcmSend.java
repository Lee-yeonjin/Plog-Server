package com.plog.server.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FcmSend {
    private String targetToken;
    private String title;
    private String body;
}