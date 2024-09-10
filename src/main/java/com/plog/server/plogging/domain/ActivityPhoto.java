package com.plog.server.plogging.domain;

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
@Table(name = "ACTIVITY_PHOTO_TABLE")
public class ActivityPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityPhotoId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id",nullable = false)
    private Activity activity;

    private String activityPhotoName;

    private String actitityPhotoS3Key;
}
