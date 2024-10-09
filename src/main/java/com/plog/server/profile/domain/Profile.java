package com.plog.server.profile.domain;

import com.plog.server.badge.domain.Badge;
import com.plog.server.badge.domain.MyBadge;
import com.plog.server.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
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

    Integer totalTime;

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

    @PrePersist
    protected void onCreate() {
        if (this.totalDistance == null) this.totalDistance = 0.0;
        if (this.totalTrash == null) this.totalTrash = 0;
        if (this.totalTime == null) this.totalTime = 0;
        if (this.totalCoin == null) this.totalCoin = 0;
        this.userMembership = this.userMembership;
        this.ploggingStatus = this.ploggingStatus;
    }
    public void setTotalCoin(int totalCoin) { this.totalCoin = totalCoin; }
    public void addToTotalDistance(Double distance) { this.totalDistance += distance; }
    public void addToTotalTime(Integer time) { this.totalTime += time; }
    public void setSelectedBadge(Badge badge) {
        this.badge = badge;
    }
    public void setPloggingStatus(boolean b) {
        this.ploggingStatus = b;
    }
    public void setIncreaseCoins(Integer coin) {
        this.totalCoin += coin;
    }
    public void setUserMembership(boolean b){this.userMembership = b; }
}
