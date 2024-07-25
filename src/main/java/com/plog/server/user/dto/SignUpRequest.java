package com.plog.server.user.dto;

import lombok.Data;

@Data
public class SignUpRequest {

    private String account;

    private String password;

    private String nickname;

    private String email;

    private Boolean isEmailVerifien = false;
}
