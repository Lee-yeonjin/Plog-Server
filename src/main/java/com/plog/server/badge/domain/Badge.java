package com.plog.server.badge.domain;

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
@Table(name = "Badge_Table")
public class Badge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long badgeId;

    String badgeGoal;

    Integer cost;
}
