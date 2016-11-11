package com.github.wakingrufus.elo.game;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class GameRecordTest {
    @Test
    public void toDto() throws Exception {

        // empty object
        Game expected = Game.builder().build();
        GameRecord input = GameRecord.builder().build();
        Game actual = input.toDto();
        Assert.assertEquals("empty object converts correctly", expected, actual);

        // populated object
        String id = UUID.randomUUID().toString();
        ZonedDateTime expectedEntryDate = ZonedDateTime.now(ZoneOffset.UTC);
        Date inputEntryDate = Date.from(expectedEntryDate.toInstant());
        expected = Game.builder()
                .id(id)
                .team1Score(1)
                .entryDate(expectedEntryDate)
                .build();
        input = GameRecord.builder()
                .id(id)
                .team1Score(1)
                .entryDate(inputEntryDate)
                .build();
        actual = input.toDto();
        log.info("expected: " + expected.toString());
        log.info("actual:   " + actual.toString());
        Assert.assertEquals("populated object converts correctly", expected, actual);
    }

    @Test
    public void testLombok() {
        GameRecord instance = GameRecord.builder().id(UUID.randomUUID().toString()).build();
        GameRecord instance2 = instance.toBuilder().build();
        Assert.assertEquals(instance.hashCode(), instance2.hashCode());
        Assert.assertEquals(instance, instance2);
    }

}