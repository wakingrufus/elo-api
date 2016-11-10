package com.github.wakingrufus.elo.ratinghistory;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

@Slf4j
public class RatingHistoryItemRecordTest {
    @Test
    public void toDto() throws Exception {

        // empty object
        RatingHistoryItem expected = RatingHistoryItem.builder().build();
        RatingHistoryItemRecord input = RatingHistoryItemRecord.builder().build();
        RatingHistoryItem actual = input.toDto();
        Assert.assertEquals("empty object converts correctly", expected, actual);

        // populated object
        String gameId = UUID.randomUUID().toString();
        String playerId = UUID.randomUUID().toString();
        String id = UUID.randomUUID().toString();
        expected = RatingHistoryItem.builder()
                .gameId(gameId)
                .startingRating(1600)
                .ratingAdjustment(23)
                .playerId(playerId)
                .id(id)
                .build();
        input = RatingHistoryItemRecord.builder()
                .gameId(gameId)
                .startingRating(1600)
                .ratingAdjustment(23)
                .playerId(playerId)
                .id(id)
                .build();
        actual = input.toDto();
        log.info("Expected: " + expected.toString());
        log.info("Actual:   " + actual.toString());
        Assert.assertEquals("populated object converts correctly", expected, actual);
    }

}