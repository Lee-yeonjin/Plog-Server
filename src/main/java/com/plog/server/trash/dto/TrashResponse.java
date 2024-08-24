package com.plog.server.trash.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class TrashResponse {
    private Long trashId;
    private int garbage;
    private int can;
    private int plastic;
    private int paper;
    private int plastic_bag;
    private int glass;
    private int trashSum;
    private LocalDate ploggingDate;
    private String photo="duswls@naver/cpm";
}