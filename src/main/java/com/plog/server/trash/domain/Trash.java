package com.plog.server.trash.domain;

import com.plog.server.plogging.domain.Activity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Trash_Table")
public class Trash {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trashId;

    private int garbage; // 일반쓰레기

    private int can; // 캔,고철

    private int plastic; // 플라스틱

    private int paper; // 종이

    private int plastic_bag; // 비닐

    private int glass; // 유리

    private int trash_sum; // 전체 쓰레기 개수


    @OneToOne // 일대일 관계로 수정
    @JoinColumn(name = "activity_id", nullable = false) // 외래 키 설정
    private Activity activity;

    @PrePersist
    public void prePersist() {
        this.trash_sum = calculateTrashSum();
    }

    private int calculateTrashSum() {
        return garbage + can + plastic + paper + plastic_bag + glass;
    }
}