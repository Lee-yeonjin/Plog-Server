package com.plog.server.plogging.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;


@Data
public class ActivityRequest {
    Double distance;
    Integer acitvityTime;
}
