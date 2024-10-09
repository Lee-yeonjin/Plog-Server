package com.plog.server.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "User_Table")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private UUID userUUID;

    @Column(unique = true)
    private String userAccount;

    @Column(nullable = false)
    private String userPw;

    @Column(unique = true)
    private String userEmail;

}
