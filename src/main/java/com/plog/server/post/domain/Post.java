package com.plog.server.post.domain;

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
@Table(name = "Post_Table")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    private String title;

    private String content;

    private int joinCount;

    private int likeCount;

    private String plogPlace;

    private String meetPlace;

    private LocalDate time;

    private String schedule;

    private double ploggingLatitude;

    private double ploggingLongitude;

    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @PrePersist
    public void prePersist() {
        this.joinCount = 0; // 참여를 몇 명 눌렀는지
        this.likeCount = 0;
        this.time = LocalDate.now();
    }
}
