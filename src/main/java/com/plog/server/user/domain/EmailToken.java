package com.plog.server.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "EmailToken_Table")
public class EmailToken {
    // 이메일 토큰 만료 시간
    private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 5L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String emailUuid;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private UserTemp userTemp;

    private LocalDateTime expirationDate;

    private boolean expired;

    //이메일 인증 토큰 생성
    public  static  EmailToken createEmailToken(UserTemp userTemp){
        EmailToken emailToken = EmailToken.builder()
                .expirationDate(LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE))
                .expired(false)
                .userTemp(userTemp)
                .build();

        return emailToken;
    }
}
