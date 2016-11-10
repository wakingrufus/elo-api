package com.github.wakingrufus.elo.game;

import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Value
@Builder(toBuilder = true)
public class Game {

    private final String id;
    private final String leagueId;
    private final int team1Score;
    private final int team2Score;
    private final ZonedDateTime entryDate;
    private final List<String> team1PlayerIds;
    private final List<String> team2PlayerIds;

    public GameRecord toRecord() {
        Date entryDate = null;
        if (getEntryDate() != null) {
            entryDate = Date.from(getEntryDate().toInstant());
        }
        return GameRecord.builder()
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
