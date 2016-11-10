package com.github.wakingrufus.elo.ratinghistory;


import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class RatingHistoryItem {
    private final String id;
    private final String playerId;
    private final String gameId;
    private final int ratingAdjustment;
    private final int startingRating;

    public RatingHistoryItemRecord toRecord() {
        return RatingHistoryItemRecord.builder()
                .id(getId())
                .gameId(getGameId())
                .playerId(getPlayerId())
                .ratingAdjustment(getRatingAdjustment())
                .startingRating(getStartingRating())
                .build();
    }
}
