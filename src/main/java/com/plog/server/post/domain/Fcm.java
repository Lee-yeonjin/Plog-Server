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
@Table(name = "Fcm_Table")
public class Fcm{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long FcmId;

    private boolean notificationEnabled = false; // 알림 설정 여부
    private Double latitude; // 알림 위도
    private Double longitude; // 알림 경도
    private String deviceToken; // 디바이스 토큰

    @OneToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
}
