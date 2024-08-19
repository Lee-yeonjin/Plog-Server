package com.plog.server.profile.domain;

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
@Table(name = "Feedback_Table")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long feedbackId;

    String feedbackContent;

    private double basketLatitude;

    private double basketLongitude;
}
