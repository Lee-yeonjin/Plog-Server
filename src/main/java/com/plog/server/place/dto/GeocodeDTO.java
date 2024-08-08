package com.plog.server.place.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GeocodeDTO {

    private double latitude1;
    private double latitude2;
    private double longitude1;
    private double longitude2;
    private double distance;
    private int time;


    private String place;
    private String zip;
    private String city;
    private String province;
    private String region;
}