package com.github.wakingrufus.elo.game;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@DynamoDBTable(tableName = "Game")
public class GameRecord {
    @DynamoDBHashKey
    private String id;
    private String leagueId;
    private Date entryDate;
    private List<String> team1PlayerIds;
    private List<String> team2PlayerIds;
    private int team1Score;
    private int team2Score;


    public Game toDto() {
        ZonedDateTime entryDate = null;
        if (getEntryDate() != null) {
            entryDate = getEntryDate().toInstant().atZone(ZoneOffset.UTC);
        }
        return Game.builder()
                .id(getId())
                .leagueId(getLeagueId())
                .team1Score(getTeam1Score())
                .team2Score(getTeam2Score())
                .entryDate(entryDate)
                .team1PlayerIds(getTeam1PlayerIds())
                .team2PlayerIds(getTeam2PlayerIds())
                .build();
    }
}
