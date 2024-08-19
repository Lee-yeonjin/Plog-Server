package com.plog.server.trash.domain;

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
@Table(name = "Basket_table")
public class Basket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long basketId;

    private double basketLatitude;

    private double basketLongitude;
}
