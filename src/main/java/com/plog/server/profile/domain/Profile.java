package com.plog.server.profile.domain;

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

    Double totaldistance;

    Integer totalTrash;

    Double totalTime;

    Integer totalCoin;
}
