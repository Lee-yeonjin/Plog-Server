package com.plog.server.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequest {
    private String userAccount;
    private String userPw;
    private String deviceToken;
}