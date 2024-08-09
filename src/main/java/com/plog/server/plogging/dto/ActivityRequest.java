package com.plog.server.plogging.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;


@Data
public class ActivityRequest {
    Double distance;
    Integer acitvityTime;

    private double latitude1;
    private double longitude2;

    private double longitude1;
    private double latitude2;

    private String place;

}
