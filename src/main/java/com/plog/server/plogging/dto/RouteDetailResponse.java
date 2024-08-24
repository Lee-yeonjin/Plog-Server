package com.plog.server.plogging.dto;

import com.plog.server.plogging.domain.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RouteDetailResponse {
    private List<Location> locations;
}
