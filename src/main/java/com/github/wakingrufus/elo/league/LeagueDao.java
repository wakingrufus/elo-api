package com.github.wakingrufus.elo.league;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.github.wakingrufus.elo.tech.db.DynamoDbClientFactory;
import com.github.wakingrufus.elo.tech.db.GenericDynamoDbDao;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Slf4j
@Service
@Singleton
public class LeagueDao extends GenericDynamoDbDao<LeagueRecord, String> {

    @Inject
    public LeagueDao(DynamoDbClientFactory clientFactory) {
        super(clientFactory, LeagueRecord.class);
        log.debug("initialized LeagueDao");
    }

    public List<LeagueRecord> byType(GameType gameType) {
        PaginatedQueryList<LeagueRecord> results = tableMapper.query(new DynamoDBQueryExpression<LeagueRecord>()
                .withScanIndexForward(true)
                .withLimit(100)
                .withConsistentRead(false)
                .withHashKeyValues(LeagueRecord.builder().gameType(gameType).build())
                .withIndexName("LeagueByType"));
        results.loadAllResults();
        return results;
    }
}
