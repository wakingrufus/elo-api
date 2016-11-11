package com.github.wakingrufus.elo.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wakingrufus.elo.tech.ObjectMapperFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class GameTest {
    @Test
    public void toRecord() throws Exception {

        // empty object
        log.info("test empty object");
        GameRecord expected = GameRecord.builder().build();
        Game input = Game.builder().build();
        GameRecord actual = input.toRecord();
        Assert.assertEquals("empty object converts correctly", expected, actual);

        // populated object
        log.info("test populated object");
        String id = UUID.randomUUID().toString();
        Date entryDate = Date.from(Instant.now());
        ZonedDateTime entryDateZoned = ZonedDateTime.from(entryDate.toInstant().atZone(ZoneOffset.UTC));
        expected = GameRecord.builder()
                .id(id)
                .entryDate(entryDate)
                .build();
        input = Game.builder()
                .id(id)
                .entryDate(entryDateZoned)
                .build();
        actual = input.toRecord();
        log.info("expected: " + expected.toString());
        log.info("actual:   " + actual.toString());
        Assert.assertEquals("converts entryDate correctly", expected.getEntryDate(), actual.getEntryDate());
        Assert.assertEquals("converts populated object correctly", expected, actual);
    }

    @Test
    public void testSerialization() throws IOException {
        ObjectMapper objectMapper = new ObjectMapperFactory().buildObjectMapper();
        Game expected = Game.builder().entryDate(ZonedDateTime.now()).build();
        String json = objectMapper.writeValueAsString(expected);
        log.info("json: " + json);
        Game actual = objectMapper.readValue(json, Game.class);
        Assert.assertEquals(expected, actual);


    }

    @Test
    public void testLombok() {
        Game game = Game.builder().build();
        Game game2 = game.toBuilder().build();
        Assert.assertEquals(game.hashCode(), game2.hashCode());
    }
}