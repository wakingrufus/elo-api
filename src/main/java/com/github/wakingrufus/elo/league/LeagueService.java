package com.github.wakingrufus.elo.league;

import com.github.wakingrufus.elo.player.Player;
import com.github.wakingrufus.elo.player.PlayerService;
import org.jvnet.hk2.annotations.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class LeagueService {
    private final LeagueDao leagueDao;
    private final PlayerService playerService;
    private final LeagueLookupDao leagueLookupDao;

    public LeagueService(LeagueDao leagueDao, PlayerService playerService, LeagueLookupDao leagueLookupDao) {
        this.leagueDao = leagueDao;
        this.playerService = playerService;
        this.leagueLookupDao = leagueLookupDao;
    }

    public GameType[] listGameTypes() {
        return GameType.values();
    }

    public List<League> getLeaguesForGameType(GameType gameType) {
        Collection<LeagueLookup> lookupRecords = leagueLookupDao.findByPartition(gameType.toString());
        List<League> leagues = new ArrayList<>();
        for (LeagueLookup leagueLookupRecord : lookupRecords) {
            leagues.add(leagueDao.findOne(leagueLookupRecord.getId()).toDto());
        }
        return leagues;
    }

    public League createLeague(League league) {
        LeagueRecord toCreate = league.toRecord();
        LeagueRecord createdRecord = leagueDao.create(toCreate);

        Player leader = playerService.create(Player.builder()
                .id(UUID.randomUUID().toString())
                .admin(true)
                .currentRating(createdRecord.getStartingRating())
                .leagueId(createdRecord.getId())
                .userId(createdRecord.getLeaderUserId())
                .build());

        return createdRecord.toDto();
    }

    public League getLeague(String id) {
        return leagueDao.findOne(id).toDto();
    }
}
