package com.plog.server.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginResponse {
    private UUID userUUID;
    private String userNickname;
}
