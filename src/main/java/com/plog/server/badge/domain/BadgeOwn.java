package com.plog.server.badge.domain;

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
@Table(name = "BadgeOwn")
public class BadgeOwn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long badgeOwnId;
}
