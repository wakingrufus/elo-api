package com.github.wakingrufus.elo.user;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.github.wakingrufus.elo.tech.db.DynamoDbClientFactory;
import com.github.wakingrufus.elo.tech.db.GenericDynamoDbDao;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Service
@Singleton
public class UserDao extends GenericDynamoDbDao<UserRecord, String> {

    @Inject
    public UserDao(DynamoDbClientFactory clientFactory) {
        super(clientFactory, UserRecord.class);
        log.info("initialized UserDao");
    }

    public UserRecord byEmail(String email) {
        UserRecord lookup = null;
        PaginatedQueryList<UserRecord> results = tableMapper.query(new DynamoDBQueryExpression<UserRecord>()
                .withLimit(1)
                .withConsistentRead(false)
                .withHashKeyValues(UserRecord.builder().email(email).build())
                .withIndexName("UserByEmail"));
        if (results.size() > 0) {
            lookup = results.get(0);
        }
        return lookup;
    }
}
