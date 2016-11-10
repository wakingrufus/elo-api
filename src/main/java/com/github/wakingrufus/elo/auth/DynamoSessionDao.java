package com.github.wakingrufus.elo.auth;

import com.github.wakingrufus.elo.tech.db.DynamoDbClientFactory;
import com.github.wakingrufus.elo.tech.db.GenericDynamoDbDao;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

@Slf4j
@Service
public class DynamoSessionDao extends GenericDynamoDbDao<SessionRecord, String> implements SessionDao {

    @Inject
    public DynamoSessionDao(DynamoDbClientFactory clientFactory) {
        super(clientFactory, SessionRecord.class);
        log.info("initialized DynamoSessionDao");
    }


}
