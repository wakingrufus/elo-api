package com.github.wakingrufus.elo.user;

import com.github.wakingrufus.elo.tech.db.DynamoDbClientFactory;
import com.github.wakingrufus.elo.tech.db.GenericDynamoDbDao;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

@Slf4j
@Service
public class DynamoUserEmailLookupDao extends GenericDynamoDbDao<UserEmailLookup, String> {

    @Inject
    public DynamoUserEmailLookupDao(DynamoDbClientFactory clientFactory) {
        super(clientFactory, UserEmailLookup.class);
        log.info("initialized DynamoUserEmailLookupDao");
    }
}
