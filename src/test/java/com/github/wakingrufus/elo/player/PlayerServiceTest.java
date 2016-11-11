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
        PlayerLookupDao playerLookupDao = Mockito.mock(PlayerLookupDao.class);
        Mockito.when(playerDao.create(Mockito.any(PlayerRecord.class))).then(AdditionalAnswers.returnsFirstArg());
        Mockito.when(playerLookupDao.create(Mockito.any(PlayerLookup.class))).then(AdditionalAnswers.returnsFirstArg());

        Player toCreate = Player.builder()
                .userId(UUID.randomUUID().toString())
                .build();

        PlayerService instance = new PlayerService(playerDao, playerLookupDao);
        Player created = instance.create(toCreate);

        Assert.assertEquals("user id is saved", toCreate.getUserId(), created.getUserId());
        Assert.assertNotNull("id is generated", created.getId());
    }

    @Test
    public void update() throws Exception {

    }

    @Test
    public void reset() throws Exception {

    }

    @Test
    public void getPlayer() throws Exception {

    }

}