package com.github.wakingrufus.elo.league;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

@Slf4j
public class LeagueRecordTest {
    @Test
    public void toDto() throws Exception {
        // empty object
        League expected = League.builder().build();
        LeagueRecord input = LeagueRecord.builder().build();
        League actual = input.toDto();
        Assert.assertEquals("converts empty object correctly", expected, actual);

        // populated object
        String id = UUID.randomUUID().toString();
        String leaderId = UUID.randomUUID().toString();
        expected = League.builder()
                .id(id)
                .leaderId(leaderId)
                .startingRating(1600)
                .name("name")
                .gameType(GameType.FOOSBALL)
                .kfactorBase(2)
                .teamSize(1)
                .trialKFactorMultiplier(2)
                .trialPeriod(10)
                .xi(6)
                .build();
        input = LeagueRecord.builder()
                .id(id)
                .gameType(GameType.FOOSBALL)
                .kFactor(2)
                .leaderUserId(leaderId)
                .startingRating(1600)
                .name("name")
                .teamSize(1)
                .trialKFactorMultiplier(2)
                .trialPeriod(10)
                .xi(6)
                .build();
        actual = input.toDto();

        log.info("Input: " + input.toString());
        log.info("Expected: " + expected.toString());
        log.info("Actual:   " + actual.toString());
        Assert.assertEquals("copies id value", id, actual.getId());

        // round trip
        Assert.assertEquals("converts populated object correctly", expected, actual);
    }

}