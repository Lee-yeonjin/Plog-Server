package com.plog.server.post.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FcmSend {
    private String targetToken;
    private String title;
    private String body;
}