package com.plog.server.trash.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrashDto {

    private int garbage;
    private int can;
    private int plastic;
    private int paper;
    private int plastic_bag;
    private int glass;
}