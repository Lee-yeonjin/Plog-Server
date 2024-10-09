package com.plog.server.post.domain;

import com.plog.server.profile.domain.Profile;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Join_Table")
public class Join {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long joinId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
}
