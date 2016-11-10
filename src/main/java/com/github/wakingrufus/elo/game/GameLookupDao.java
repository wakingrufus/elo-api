package com.github.wakingrufus.elo.game;

import com.github.wakingrufus.elo.tech.db.DynamoDbClientFactory;
import com.github.wakingrufus.elo.tech.db.GenericDynamoDbRangeDao;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

@Slf4j
@Service
public class GameLookupDao extends GenericDynamoDbRangeDao<GameLookup, String, String> {

    @Inject
    public GameLookupDao(DynamoDbClientFactory clientFactory) {
        super(clientFactory, GameLookup.class);
        log.info("initialized GameLookupDao");
    }
}
