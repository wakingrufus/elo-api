package com.github.wakingrufus.elo.league;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class League {
    private final String id;
    private final String name;
    private final String leaderId;
    private final int startingRating; // 1500 is typical
    private final int xi; // 1000 is typical
    private final int kfactorBase;  // 32 is typical
    private final int trialPeriod; // 10 is typical
    private final int trialKFactorMultiplier; // 2 is typical
    private final GameType gameType;
    private final int teamSize;

    LeagueRecord toRecord() {
        return LeagueRecord.builder()
                .id(getId())
                .name(getName())
                .leaderUserId(getLeaderId())
                .startingRating(getStartingRating())
                .xi(getXi())
                .kFactor(getKfactorBase())
                .gameType(getGameType())
                .teamSize(getTeamSize())
                .trialKFactorMultiplier(getTrialKFactorMultiplier())
                .trialPeriod(getTrialPeriod())
                .build();
    }
}
