package com.plog.server.user.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "EmailToken_Table")
public class EmailToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailId;

    private UUID emailUuid;

    @OneToOne
    @JoinColumn(name = "tempId", nullable = false)
    private UserTemp userTemp;

    private LocalDateTime expirationDate;

    private boolean expired;
}
