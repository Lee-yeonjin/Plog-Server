package com.plog.server.mission.domain;

import com.plog.server.profile.domain.Profile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "User_Mission_Table")
public class UserMission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long UserMissionId;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;

    private LocalDate createdDay;

    private Integer missionCoin;

    private boolean isFinish;

    @PrePersist
    protected void onCreate() {
        this.isFinish = false;
    }
}