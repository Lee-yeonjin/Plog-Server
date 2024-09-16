package com.plog.server.rank.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RankResponse {
    private String userNickname;
    private int score;
    private int rank;
    private int badge;

    private String myUsername;
    private int myRank;
    private int myScore;

    public RankResponse(String userNickname, int score, int rank, int badge) {
        this.userNickname = userNickname;
        this.score = score;
        this.rank = rank;
        this.badge = badge;
    }
}
