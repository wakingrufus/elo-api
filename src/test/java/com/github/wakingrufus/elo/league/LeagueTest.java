package com.github.wakingrufus.elo.league;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wakingrufus.elo.tech.ObjectMapperFactory;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

public class LeagueTest {
    @Test
    public void testJson() throws IOException {
        League league = League.builder()
                .id(UUID.randomUUID().toString())
                .name("name")
                .leaderId(UUID.randomUUID().toString())
                .kfactorBase(16)
                .startingRating(1600)
                .gameType(GameType.FOOSBALL)
                .teamSize(1)
                .trialKFactorMultiplier(2)
                .trialPeriod(10)
                .build();
        ObjectMapper objectMapper = new ObjectMapperFactory().buildObjectMapper();
        String json = objectMapper.writeValueAsString(league);
        League actual = objectMapper.readValue(json, League.class);
        Assert.assertEquals(league, actual);
    }

    @Test
    public void testToRecord() throws Exception {
        String id = UUID.randomUUID().toString();
        int kFactor = 32;
        League input = League.builder()
                .id(id)
                .kfactorBase(kFactor)
                .build();
        LeagueRecord actual = input.toRecord();
        Assert.assertEquals("copies id value", id, actual.getId());
        Assert.assertEquals("copies kfactor value", kFactor, actual.getKFactor());


        // round trip
        Assert.assertEquals("round trip retains all data", input, actual.toDto());
    }
}