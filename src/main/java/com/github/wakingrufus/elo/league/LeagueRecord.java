package com.github.wakingrufus.elo.league;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@DynamoDBTable(tableName = "EloLeague")
public class LeagueRecord {
    @DynamoDBHashKey
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = "LeagueByType")
    private String id;
    private String name;
    private String leaderUserId;
    private int startingRating;
    private int xi;
    private int kFactor;
    @DynamoDBTypeConvertedEnum
    @DynamoDBIndexHashKey(globalSecondaryIndexName = "LeagueByType")
    private GameType gameType;
    private int trialPeriod;
    private int trialKFactorMultiplier;
    private int teamSize;

    League toDto() {
        return League.builder()
                .id(getId())
                .name(getName())
                .leaderId(getLeaderUserId())
                .startingRating(getStartingRating())
                .xi(getXi())
                .kfactorBase(getKFactor())
                .gameType(getGameType())
                .teamSize(getTeamSize())
                .trialKFactorMultiplier(getTrialKFactorMultiplier())
                .trialPeriod(getTrialPeriod())
                .build();
    }
}
