package com.github.wakingrufus.elo.ratinghistory;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@DynamoDBTable(tableName = "RatingHistoryItem")
public class RatingHistoryItemRecord {
    @DynamoDBHashKey
    private String id;
    private String playerId;
    private String gameId;
    private int startingRating;
    private int ratingAdjustment;

    public RatingHistoryItem toDto() {
        return RatingHistoryItem.builder()
                .id(getId())
                .gameId(getGameId())
                .playerId(getPlayerId())
                .ratingAdjustment(getRatingAdjustment())
                .startingRating(getStartingRating())
                .build();
    }
}
