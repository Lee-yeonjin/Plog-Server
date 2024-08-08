package com.plog.server.place.domain;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Place_Table")
public class Place{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long placeId;

    private double latitude1;

    private double longitude1;

    private double latitude2;

    private double longitude2;

    private String place1;

    private String place2;

}