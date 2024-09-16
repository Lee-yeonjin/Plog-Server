package com.plog.server.rank.domain;

import com.plog.server.profile.domain.Profile;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Rank_table")
public class Rank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long RankId;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    private Double distance;

    private int trash_sum;
}
