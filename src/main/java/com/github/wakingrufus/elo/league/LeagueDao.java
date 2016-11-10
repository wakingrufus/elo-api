package com.github.wakingrufus.elo.league;

import com.github.wakingrufus.elo.tech.db.DynamoDbClientFactory;
import com.github.wakingrufus.elo.tech.db.GenericDynamoDbDao;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

@Slf4j
@Service
public class LeagueDao extends GenericDynamoDbDao<LeagueRecord, String>  {

    @Inject
    public LeagueDao(DynamoDbClientFactory clientFactory) {
        super(clientFactory, LeagueRecord.class);
        log.info("initialized DynamoUserDao");
    }
}
