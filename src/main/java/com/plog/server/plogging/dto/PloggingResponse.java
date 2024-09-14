package com.plog.server.plogging.dto;

import com.plog.server.plogging.domain.Activity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PloggingResponse {
    private Long activityId;
    private LocalDate ploggingDate;
    private Integer ploggingTime;
    private Double distance;
    private int trash_sum;

    public PloggingResponse(Activity activity) {
        this.activityId = activity.getActivityId();
        this.ploggingDate = activity.getPloggingDate();
        this.ploggingTime = activity.getPloggingTime();
        this.distance = activity.getDistance();
        this.trash_sum = (activity.getTrash() != null) ? activity.getTrash().getTrash_sum() : 0;
    }
}
