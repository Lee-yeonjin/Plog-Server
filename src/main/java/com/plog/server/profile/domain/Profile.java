package com.plog.server.profile.domain;

import com.plog.server.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Profile_table")
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long profileId;

    String userNickname;

    Double totalDistance;

    Integer totalTrash;

    Double totalTime;

    Integer totalCoin;

    private boolean userMembership;

    private boolean ploggingStatus;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public void setPloggingStatus(boolean b) {
        this.ploggingStatus = b;
    }

}
