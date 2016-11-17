package com.github.wakingrufus.elo.league;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wakingrufus.elo.tech.ObjectMapperFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

@Slf4j
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
        LeagueRecord expected = LeagueRecord.builder()
                .id(id)
                .kFactor(kFactor)
                .build();
        LeagueRecord actual = input.toRecord();
        Assert.assertEquals("copies id value", id, actual.getId());
        Assert.assertEquals("copies kfactor value", kFactor, actual.getKFactor());


        // round trip
        Assert.assertEquals("converts populated object", expected, actual);
    }

    @Test
    public void testLombok() {
        League league = League.builder().build();
        League league2 = league.toBuilder().build();
        Assert.assertEquals(league.hashCode(), league2.hashCode());
        String s = League.builder().toString();
        log.trace(s);
    }
}