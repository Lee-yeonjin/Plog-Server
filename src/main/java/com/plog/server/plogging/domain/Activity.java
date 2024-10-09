package com.plog.server.plogging.domain;

import com.plog.server.profile.domain.Profile;
import com.plog.server.trash.domain.Trash;
import com.plog.server.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Activity_Table")
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Location> locations;

    private Integer ploggingTime;

    private Double distance;

    private Boolean routeStatus = false;

    private String startPlace;

    private String endPlace;

    private LocalDate ploggingDate;

    @OneToOne(mappedBy = "activity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Trash trash;

    public void setRouteStatus(){
        this.routeStatus = Boolean.TRUE;
    }

}

