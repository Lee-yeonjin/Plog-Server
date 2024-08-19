package com.plog.server.mission.domain;

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
@Table(name = "Mission_Table")
public class Mission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long missionId;

    Integer missionCoin;

    String mission1;

    String mission2;

    String mission3;
}
