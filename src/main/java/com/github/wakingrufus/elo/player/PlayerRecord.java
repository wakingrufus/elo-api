package com.github.wakingrufus.elo.player;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@DynamoDBTable(tableName = "EloPlayer")
public class PlayerRecord {
    @DynamoDBHashKey
    @DynamoDBIndexRangeKey(globalSecondaryIndexNames = {"PlayerByUser", "PlayerByLeague"})
    private String id;
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "PlayerByUser")
    private String userId;
    private boolean admin;
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "PlayerByLeague")
    private String leagueId;
    private int currentRating;
    private int gamesPlayed;
    private int wins;
    private int losses;

    Player toDto() {
        return Player.builder()
                .id(getId())
                .userId(getUserId())
                .leagueId(getLeagueId())
                .admin(isAdmin())
                .currentRating(getCurrentRating())
                .gamesPlayed(gamesPlayed)
                .wins(getWins())
                .losses(getLosses())
                .build();
    }
}
