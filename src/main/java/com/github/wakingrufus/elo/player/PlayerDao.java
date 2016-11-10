package com.github.wakingrufus.elo.player;

import com.github.wakingrufus.elo.tech.db.DynamoDbClientFactory;
import com.github.wakingrufus.elo.tech.db.GenericDynamoDbDao;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

@Service
@Slf4j
public class PlayerDao extends GenericDynamoDbDao<PlayerRecord, String> {
    @Inject
    public PlayerDao(DynamoDbClientFactory clientFactory) {
        super(clientFactory, PlayerRecord.class);
        log.info("initialized PlayerDao");
    }
}
