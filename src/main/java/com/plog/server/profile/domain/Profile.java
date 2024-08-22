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

    Integer totalTrash;

    Double totalTime;

    Integer totalCoin;

    String userNickname;

    Double totalDistance;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    private boolean userMembership;

    private boolean userPloggingStatus;

}
