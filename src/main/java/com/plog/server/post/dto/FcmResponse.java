package com.plog.server.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FcmResponse{
    private String targetToken;
    private String title;
    private String body;
}