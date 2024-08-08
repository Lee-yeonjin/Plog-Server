package com.plog.server.post.domain;

import com.plog.server.user.domain.User;
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

    private String place;

    private LocalDate time;

    private String schedule;

    @ManyToOne
    private User user;

    @PrePersist
    public void prePersist() {
        this.joinCount = 0; // 참여를 몇 명 눌렀는지
        this.time = LocalDate.now();
    }
}
