package com.github.wakingrufus.elo.game;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.time.Instant;
import java.util.Date;

@Slf4j
public class GameRecordTest {
    @Test
    public void toDto() throws Exception {

        // empty object
        GameRecord gameRecord = GameRecord.builder().build();
        Game game = gameRecord.toDto();
        GameRecord actual = game.toRecord();
        Assert.assertEquals("empty object converts correctly", gameRecord, actual);

        // populated object
        gameRecord = GameRecord.builder().entryDate(Date.from(Instant.now())).build();
        game = gameRecord.toDto();
        actual = game.toRecord();
        Assert.assertEquals("populated object converts correctly", gameRecord, actual);
    }


}