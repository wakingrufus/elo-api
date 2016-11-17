package com.github.wakingrufus.elo.league;

import com.github.wakingrufus.elo.player.Player;
import com.github.wakingrufus.elo.player.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Singleton
@Slf4j
public class LeagueService {
    private final LeagueDao leagueDao;
    private final PlayerService playerService;

    @Inject
    public LeagueService(LeagueDao leagueDao, PlayerService playerService) {
        this.leagueDao = leagueDao;
        this.playerService = playerService;
    }

    public List<GameType> listGameTypes() {
        return Arrays.asList(GameType.values());
    }

    public List<League> getLeaguesForGameType(GameType gameType) {
        List<LeagueRecord> leagueLookupRecords = leagueDao.byType(gameType);
        List<League> leagues = new ArrayList<>();
        for (LeagueRecord leagueLookupRecord : leagueLookupRecords) {
            leagues.add(leagueDao.findOne(leagueLookupRecord.getId()).toDto());
        }
        return leagues;
    }

    public League createLeague(League league) {
        LeagueRecord toCreate = league.toRecord().toBuilder().id(UUID.randomUUID().toString()).build();
        LeagueRecord createdRecord = leagueDao.create(toCreate);

        Player leader = playerService.create(Player.builder()
                .id(UUID.randomUUID().toString())
                .admin(true)
                .currentRating(createdRecord.getStartingRating())
                .leagueId(createdRecord.getId())
                .userId(createdRecord.getLeaderUserId())
                .build());

        log.info("Created leader[" + leader.getId() + "] for league[" + league.getName() + "] for user: " + createdRecord.getLeaderUserId());

        return createdRecord.toDto();
    }

    public League getLeague(String id) {
        return leagueDao.findOne(id).toDto();
    }
}
