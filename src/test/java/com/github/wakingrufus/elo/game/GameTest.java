package com.github.wakingrufus.elo.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wakingrufus.elo.tech.ObjectMapperFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Slf4j
public class GameTest {
    @Test
    public void toRecord() throws Exception {

        // empty object
        Game expected = Game.builder().build();
        GameRecord converted = expected.toRecord();
        Game actual = converted.toDto();
        Assert.assertEquals("empty object converts correctly", expected, actual);

        // populated object
        expected = Game.builder().entryDate(ZonedDateTime.now(ZoneOffset.UTC)).build();
        converted = expected.toRecord();
        actual = converted.toDto();
        Assert.assertEquals("populated object converts correctly", expected, actual);
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
}