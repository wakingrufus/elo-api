package com.github.wakingrufus.elo.player;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.Mockito;

import java.util.UUID;

@Slf4j
public class PlayerServiceTest {
    @Test
    public void getPlayersForLeague() throws Exception {

    }

    @Test
    public void getPlayersForUser() throws Exception {

    }

    @Test
    public void create() throws Exception {
        PlayerDao playerDao = Mockito.mock(PlayerDao.class);
        Mockito.when(playerDao.create(Mockito.any(PlayerRecord.class))).then(AdditionalAnswers.returnsFirstArg());

        Player toCreate = Player.builder()
                .userId(UUID.randomUUID().toString())
                .build();

        PlayerService instance = new PlayerService(playerDao);
        Player created = instance.create(toCreate);

        Assert.assertEquals("user id is saved", toCreate.getUserId(), created.getUserId());
        Assert.assertNotNull("id is generated", created.getId());
    }

    @Test
    public void update() throws Exception {
        // data
        String id = UUID.randomUUID().toString();
        int ratingAdjustment = 32;
        int newGames = 1;
        int newWins = 1;
        int newLosses = 0;
        PlayerRecord existingRecord = PlayerRecord.builder()
                .id(id)
                .currentRating(1200)
                .gamesPlayed(1)
                .wins(1)
                .losses(0)
                .build();

        // mocks
        PlayerDao playerDao = Mockito.mock(PlayerDao.class);
        Mockito.when(playerDao.update(Mockito.any(PlayerRecord.class))).then(AdditionalAnswers.returnsFirstArg());
        Mockito.when(playerDao.findOne(id)).thenReturn(existingRecord);

        PlayerService instance = new PlayerService(playerDao);
        Player updated = instance.update(id, ratingAdjustment, newGames, newWins, newLosses);

        Assert.assertEquals(existingRecord.getCurrentRating() + ratingAdjustment, updated.getCurrentRating());
    }

    @Test
    public void reset() throws Exception {

    }

    @Test
    public void getPlayer() throws Exception {

    }

}