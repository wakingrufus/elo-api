package com.github.wakingrufus.elo.ratinghistory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wakingrufus.elo.tech.ObjectMapperFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class RatingHistoryItemTest {
    @Test
    public void toRecord() throws Exception {
        // empty object
        RatingHistoryItemRecord expected = RatingHistoryItemRecord.builder().build();
        RatingHistoryItem input = RatingHistoryItem.builder().build();
        RatingHistoryItemRecord actual = input.toRecord();
        Assert.assertEquals("empty object converts correctly", expected, actual);

        // populated object
        String gameId = UUID.randomUUID().toString();
        String playerId = UUID.randomUUID().toString();
        String id = UUID.randomUUID().toString();
        expected = RatingHistoryItemRecord.builder()
                .gameId(gameId)
                .startingRating(1600)
                .ratingAdjustment(23)
                .playerId(playerId)
                .id(id)
                .build();
        input = RatingHistoryItem.builder()
                .gameId(gameId)
                .startingRating(1600)
                .ratingAdjustment(23)
                .playerId(playerId)
                .id(id)
                .build();
        actual = input.toRecord();

        log.info("Expected: " + expected.toString());
        log.info("Actual:   " + actual.toString());
        Assert.assertEquals("populated object converts correctly", expected, actual);
    }

    @Test
    public void testSerialization() throws IOException {
        ObjectMapper objectMapper = new ObjectMapperFactory().buildObjectMapper();
        RatingHistoryItem input = RatingHistoryItem.builder()
                .gameId(UUID.randomUUID().toString())
                .startingRating(1600)
                .ratingAdjustment(23)
                .playerId(UUID.randomUUID().toString())
                .id(UUID.randomUUID().toString())
                .build();
        String json = objectMapper.writeValueAsString(input);
        log.info("json: " + json);
        RatingHistoryItem actual = objectMapper.readValue(json, RatingHistoryItem.class);
    }

}