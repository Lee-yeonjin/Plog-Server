package com.plog.server.profile.domain;

import com.plog.server.badge.domain.Badge;
import com.plog.server.badge.domain.MyBadge;
import com.plog.server.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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

    String userNickname;

    Double totalDistance;

    Integer totalTrash;

    Double totalTime;

    Integer totalCoin;

    boolean userMembership;

    boolean ploggingStatus;

    @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MyBadge> myBadges;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id")
    private Badge badge;

    public void setSelectedBadge(Badge badge) {
        this.badge = badge;
    }
    public void setPloggingStatus(boolean b) {
        this.ploggingStatus = b;
    }

    public void setIncreaseCoins(Integer coin) {
            this.totalCoin += coin;
    }
}
