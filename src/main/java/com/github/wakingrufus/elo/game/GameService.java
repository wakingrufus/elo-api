package com.github.wakingrufus.elo.game;

import com.github.wakingrufus.elo.league.League;
import org.jvnet.hk2.annotations.Contract;

import java.util.List;

@Contract
public interface GameService {
    void recalculateLeagueGames(League league);

    List<Game> getGamesForLeague(String leagueId);

    Game addGame(Game game);

    Game getById(String id);
}
