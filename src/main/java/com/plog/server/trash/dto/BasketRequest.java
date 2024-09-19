package com.plog.server.trash.dto;

import lombok.Data;

@Data
public class BasketRequest {
    private double basketLatitude;
    private double basketLongitude;
    private boolean basketIsPresent;
}
