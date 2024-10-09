package com.plog.server.plogging.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class LocationMessage {
    private UUID uuid;
    private double latitude;
    private double longitude;
}
