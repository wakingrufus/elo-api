package com.github.wakingrufus.elo.api;

import com.github.wakingrufus.elo.league.GameType;
import com.github.wakingrufus.elo.league.LeagueService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

@Slf4j
public class LeagueResourceTest {
    @Test
    public void create() throws Exception {

    }

    @Test
    public void getById() throws Exception {

    }

    @Test
    public void getByType() throws Exception {

    }

    @Test
    public void getGameTypes() throws Exception {
        LeagueService leagueService = Mockito.mock(LeagueService.class);
        Mockito.when(leagueService.listGameTypes()).thenReturn(Arrays.asList(GameType.values()));
        LeagueResource instance = new LeagueResource(leagueService);
        List<GameType> actual = instance.getGameTypes();

        Assert.assertEquals(GameType.values().length, actual.size());

    }

}