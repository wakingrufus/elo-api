package com.github.wakingrufus.elo.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wakingrufus.elo.game.Game;
import com.github.wakingrufus.elo.league.League;
import com.github.wakingrufus.elo.tech.ObjectMapperFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.UUID;

@Slf4j
public class PlayerTest {
    @Test
    public void toRecord() throws Exception {
        // test empty object
        PlayerRecord expected = PlayerRecord.builder().build();
        Player input = Player.builder().build();
        PlayerRecord actual = input.toRecord();

        log.info("expected: " + expected.toString());
        log.info("actual:   " + actual.toString());
        Assert.assertEquals("converts empty object correctly", expected, actual);
    }

    @Test
    public void testSerialization() throws IOException {
        ObjectMapper objectMapper = new ObjectMapperFactory().buildObjectMapper();
        Player expected = Player.builder().id(UUID.randomUUID().toString()).build();
        String json = objectMapper.writeValueAsString(expected);
        log.info("json: " + json);
        Player actual = objectMapper.readValue(json, Player.class);
        Assert.assertEquals(expected, actual);

    }

    @Test
    public void testLombok() {
        Player player = Player.builder().build();
        Player player2 = player.toBuilder().build();
        Assert.assertEquals(player.hashCode(), player2.hashCode());
    }
}