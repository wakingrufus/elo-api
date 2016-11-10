package com.github.wakingrufus.elo.player;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;
import java.util.UUID;

@Slf4j
public class PlayerRecordTest {
    @Test
    public void toDto() throws Exception {
        Random random = new Random();
        boolean admin = random.nextBoolean();
        int currentRating = random.nextInt();
        int gamesPlayed = random.nextInt();
        String id = UUID.randomUUID().toString();
        String leagueId = UUID.randomUUID().toString();
        int losses = random.nextInt();
        String userId = UUID.randomUUID().toString();
        int wins = random.nextInt();

        Player expected = Player.builder()
                .admin(admin)
                .currentRating(currentRating)
                .gamesPlayed(gamesPlayed)
                .id(id)
                .leagueId(leagueId)
                .losses(losses)
                .userId(userId)
                .wins(wins)
                .build();

        PlayerRecord instance = PlayerRecord.builder()
                .admin(admin)
                .currentRating(currentRating)
                .gamesPlayed(gamesPlayed)
                .id(id)
                .leagueId(leagueId)
                .losses(losses)
                .userId(userId)
                .wins(wins)
                .build();

        Player actual = instance.toDto();

        log.info("input: " + instance.toString());
        log.info("expected: " + expected.toString());
        log.info("actual: " + actual.toString());
        Assert.assertEquals(expected, actual);
    }

}