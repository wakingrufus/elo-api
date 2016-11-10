package com.github.wakingrufus.elo.player;

import com.github.wakingrufus.elo.tech.db.DynamoDbClientFactory;
import com.github.wakingrufus.elo.tech.db.GenericDynamoDbRangeDao;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

@Slf4j
@Service
public class PlayerLookupDao extends GenericDynamoDbRangeDao<PlayerLookup, String, String> {

    @Inject
    public PlayerLookupDao(DynamoDbClientFactory clientFactory) {
        super(clientFactory, PlayerLookup.class);
        log.info("initialized PlayerLookupDao");
    }
}
