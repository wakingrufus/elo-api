package com.github.wakingrufus.elo.league;

import com.github.wakingrufus.elo.tech.db.DynamoDbClientFactory;
import com.github.wakingrufus.elo.tech.db.GenericDynamoDbRangeDao;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

@Slf4j
@Service
public class LeagueLookupDao extends GenericDynamoDbRangeDao<LeagueLookup, String, String> {

    @Inject
    public LeagueLookupDao(DynamoDbClientFactory clientFactory) {
        super(clientFactory, LeagueLookup.class);
        log.info("initialized DynamoUserDao");
    }
}
