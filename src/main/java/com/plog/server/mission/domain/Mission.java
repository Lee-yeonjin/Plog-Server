package com.plog.server.mission.domain;

import com.plog.server.profile.domain.Profile;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Mission_Table")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long missionId;

    Integer missionCoin;

    String mission;
}
