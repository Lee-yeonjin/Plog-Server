package com.plog.server.plogging.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteListResponse {
    private Long ActivityId;
    private Integer ploggingTime;
    private Double distance;
    private String startPlace;
    private String endPlace;
}
