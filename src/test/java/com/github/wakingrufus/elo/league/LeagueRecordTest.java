package com.github.wakingrufus.elo.league;

import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class LeagueRecordTest {
    @Test
    public void toDto() throws Exception {
        String id = UUID.randomUUID().toString();
        LeagueRecord leagueRecord = LeagueRecord.builder()
                .id(id)
                .build();
        League actual = leagueRecord.toDto();
        Assert.assertEquals("copies id value", id, actual.getId());

        // round trip
        Assert.assertEquals("round trip retains all data", leagueRecord, actual.toRecord());
    }

}