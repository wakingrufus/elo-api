package com.github.wakingrufus.elo.player;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.github.wakingrufus.elo.league.GameType;
import com.github.wakingrufus.elo.league.LeagueRecord;
import com.github.wakingrufus.elo.tech.db.DynamoDbClientFactory;
import com.github.wakingrufus.elo.tech.db.GenericDynamoDbDao;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Service
@Slf4j
@Singleton
public class PlayerDao extends GenericDynamoDbDao<PlayerRecord, String> {
    @Inject
    public PlayerDao(DynamoDbClientFactory clientFactory) {
        super(clientFactory, PlayerRecord.class);
        log.info("initialized PlayerDao");
    }
    public List<PlayerRecord> lookupByLeague(String leagueId) {
        PaginatedQueryList<PlayerRecord> results = tableMapper.query(new DynamoDBQueryExpression<PlayerRecord>()
                .withScanIndexForward(true)
                .withLimit(100)
                .withConsistentRead(false)
                .withHashKeyValues(PlayerRecord.builder().leagueId(leagueId).build())
                .withIndexName("PlayerByLeague"));
        results.loadAllResults();
        return results;
    }

    public List<PlayerRecord> lookupByUser(String userId) {
        PaginatedQueryList<PlayerRecord> results = tableMapper.query(new DynamoDBQueryExpression<PlayerRecord>()
                .withScanIndexForward(true)
                .withLimit(100)
                .withConsistentRead(false)
                .withHashKeyValues(PlayerRecord.builder().userId(userId).build())
                .withIndexName("LeagueByType"));
        results.loadAllResults();
        return results;
    }
}
