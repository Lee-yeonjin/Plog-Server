package com.plog.server.user.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "UserTemp_Table")
public class UserTemp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tempId;

    private String tempAccount;

    private String tempPw;

    private String tempNickname;

    private String tempEmail;

    private boolean tempEmailStatus;

    @OneToOne(mappedBy = "userTemp", cascade = CascadeType.ALL, orphanRemoval = true)
    private EmailToken emailToken;
}
