package com.github.wakingrufus.elo.player;

import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PlayerService {
    private final PlayerDao playerDao;
    private final PlayerLookupDao playerLookupDao;

    @Inject
    public PlayerService(PlayerDao playerDao, PlayerLookupDao playerLookupDao) {
        this.playerDao = playerDao;
        this.playerLookupDao = playerLookupDao;
    }

    public Collection<Player> getPlayersForLeague(String leagueId) {
        Collection<PlayerLookup> lookupRecords = playerLookupDao.findByPartition(leagueId);
        List<PlayerRecord> playerRecords = lookupRecords.stream().map(lookupRecord -> playerDao.findOne(lookupRecord.getId())).collect(Collectors.toList());
        List<Player> players = playerRecords.stream().map(PlayerRecord::toDto).collect(Collectors.toList());
        return players;
    }

    public Collection<Player> getPlayersForUser(String userId) {
        Collection<PlayerLookup> lookupRecords = playerLookupDao.findByPartition(userId);
        List<PlayerRecord> playerRecords = lookupRecords.stream().map(lookupRecord -> playerDao.findOne(lookupRecord.getId())).collect(Collectors.toList());
        List<Player> players = playerRecords.stream().map(PlayerRecord::toDto).collect(Collectors.toList());
        return players;
    }

    public Player create(Player player) {
        Player toCreate = player.toBuilder().id(UUID.randomUUID().toString()).build();
        PlayerRecord createdRecord = playerDao.create(toCreate.toRecord());
        playerLookupDao.create(PlayerLookup.builder().bucket(createdRecord.getLeagueId()).id(createdRecord.getId()).build());
        playerLookupDao.create(PlayerLookup.builder().bucket(createdRecord.getUserId()).id(createdRecord.getId()).build());
        return createdRecord.toDto();
    }

    public Player update(Player player, int ratingAdjustment, int games, int wins, int losses) {
        Player newPlayer = player.toBuilder()
                .currentRating(player.getCurrentRating() + ratingAdjustment)
                .gamesPlayed(player.getGamesPlayed() + games)
                .wins(player.getWins() + wins)
                .losses(player.getLosses() + losses)
                .build();
        return playerDao.update(newPlayer.toRecord()).toDto();
    }

    public Player reset(Player player, int rating) {
        Player newPlayer = player.toBuilder()
                .currentRating(rating)
                .gamesPlayed(0)
                .wins(0)
                .losses(0)
                .build();
        return playerDao.update(newPlayer.toRecord()).toDto();
    }

    public Player getPlayer(String id) {
        return playerDao.findOne(id).toDto();
    }
}
