package com.github.wakingrufus.elo.player;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Player {

    private final String userId;
    private final boolean admin;
    private final String id;
    private final String leagueId;
    private final int currentRating;
    private final int gamesPlayed;
    private final int wins;
    private final int losses;

    PlayerRecord toRecord() {
        PlayerRecord playerRecord = PlayerRecord.builder()
                .id(getId())
                .admin(isAdmin())
                .currentRating(getCurrentRating())
                .leagueId(getLeagueId())
                .userId(getUserId())
                .build();
        return playerRecord;
    }
}
