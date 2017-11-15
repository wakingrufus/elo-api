package com.github.wakingrufus.elo.game;

import com.github.wakingrufus.elo.tech.db.DynamoDbClientFactory;
import com.github.wakingrufus.elo.tech.db.GenericDynamoDbDao;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.List;

@Slf4j
public class GameDao extends GenericDynamoDbDao<GameRecord, String> {

    @Inject
    public GameDao(DynamoDbClientFactory clientFactory) {
        super(clientFactory, GameRecord.class);
        log.info("initialized GameDao");
    }

    public List<GameRecord> byLeague(String leagueId) {
        return byIndex("GameByLeague", GameRecord.builder().leagueId(leagueId).build());
    }
}
