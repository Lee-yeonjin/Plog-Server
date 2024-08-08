package com.plog.server.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginRequestDto {
    private String userAccount;
    private String userUUID;
    private String userNickname;
    private String userPw;
}