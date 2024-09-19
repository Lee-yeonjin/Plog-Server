package com.plog.server.badge.domain;

import com.plog.server.profile.domain.Profile;
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
@Table(name = "MyBadge_Table")
public class MyBadge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long myBadgeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id", nullable = false)
    Badge badge;

//    @Column(nullable = false)
//    Boolean myBadgeStatus = true;
//
//    public boolean myBadgeStatus() {
//        // 배지의 활성 상태를 반환하는 로직
//        return this.myBadgeStatus; // active 필드가 true인 경우
//    }
}
