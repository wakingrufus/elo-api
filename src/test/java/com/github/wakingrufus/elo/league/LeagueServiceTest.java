package com.github.wakingrufus.elo.league;

import com.github.wakingrufus.elo.player.Player;
import com.github.wakingrufus.elo.player.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.AdditionalAnswers;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class LeagueServiceTest {
    @Test
    public void listGameTypes() throws Exception {
        LeagueService leagueService = new LeagueService(null, null);
        List<GameType> actual = leagueService.listGameTypes();
        Assert.assertEquals(GameType.values().length, actual.size());
    }

    @Test
    public void getLeaguesForGameType() throws Exception {
        LeagueDao leagueDao = Mockito.mock(LeagueDao.class);
        PlayerService playerService = Mockito.mock(PlayerService.class);

        String id1 = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();
        GameType gameType = GameType.FOOSBALL;

        LeagueRecord lookupRecord1 = LeagueRecord.builder().id(id1).gameType(gameType).build();
        LeagueRecord lookupRecord2 = LeagueRecord.builder().id(id2).gameType(gameType).build();
        List<LeagueRecord> lookupRecords = new ArrayList<>();
        lookupRecords.add(lookupRecord1);
        lookupRecords.add(lookupRecord2);

        Mockito.when(leagueDao.byType(GameType.FOOSBALL)).thenReturn(lookupRecords);
        Mockito.when(leagueDao.findOne(id1)).thenReturn(lookupRecord1.toBuilder().name("league 1").build());
        Mockito.when(leagueDao.findOne(id2)).thenReturn(lookupRecord2.toBuilder().name("league 2").build());

        LeagueService leagueService = new LeagueService(leagueDao, playerService);

        List<League> actuals = leagueService.getLeaguesForGameType(gameType);

        Assert.assertEquals(2, actuals.size());
    }

    @Test
    public void createLeague() throws Exception {
        LeagueDao leagueDao = Mockito.mock(LeagueDao.class);
        PlayerService playerService = Mockito.mock(PlayerService.class);

        Mockito.when(leagueDao.create(Mockito.any(LeagueRecord.class))).then(AdditionalAnswers.returnsFirstArg());
        Mockito.when(playerService.create(Mockito.any(Player.class))).then(AdditionalAnswers.returnsFirstArg());

        League toCreate = League.builder().build();

        LeagueService leagueService = new LeagueService(leagueDao, playerService);

        League actual = leagueService.createLeague(toCreate);

        Assert.assertNotNull("returns created league", actual);
        Assert.assertNotNull("created league has id", actual.getId());

        ArgumentCaptor<Player> createdPlayerCaptor = ArgumentCaptor.forClass(Player.class);
        Mockito.verify(playerService, Mockito.times(1)).create(createdPlayerCaptor.capture());
        Player createdPlayer = createdPlayerCaptor.getValue();

        Assert.assertEquals("player created in new league", actual.getId(), createdPlayer.getLeagueId());

    }

    @Test
    public void getLeague() throws Exception {

    }

}