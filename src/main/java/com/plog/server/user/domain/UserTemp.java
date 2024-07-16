package com.plog.server.user.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserTemp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tempId;

    private String tempAccount;

    private String tempPw;

    private String tempNickname;

    private String tempEmail;

    private boolean tempEmailStatus;
}
