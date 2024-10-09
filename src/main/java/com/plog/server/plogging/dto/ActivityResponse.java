package com.plog.server.plogging.dto;

import com.plog.server.plogging.domain.Activity;
import com.plog.server.plogging.domain.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityResponse {
    private Long ActivityId;

    private List<Location> locations;

    private Integer ploggingTime;

    private Double distance;

    private String startPlace;

    private String endPlace;

    public ActivityResponse(Activity activity){
        this.ActivityId = activity.getActivityId();
        this.ploggingTime = activity.getPloggingTime();
        this.distance = activity.getDistance();
        this.locations = activity.getLocations();
        this.startPlace = activity.getStartPlace();
        this.endPlace = activity.getEndPlace();
    }
}
