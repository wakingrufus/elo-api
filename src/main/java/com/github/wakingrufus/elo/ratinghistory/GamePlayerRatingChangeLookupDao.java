package com.github.wakingrufus.elo.ratinghistory;

import com.github.wakingrufus.elo.tech.db.DynamoDbClientFactory;
import com.github.wakingrufus.elo.tech.db.GenericDynamoDbRangeDao;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

@Slf4j
@Service
public class GamePlayerRatingChangeLookupDao extends GenericDynamoDbRangeDao<GamePlayerRatingChangeLookup, String, String> {

    @Inject
    public GamePlayerRatingChangeLookupDao(DynamoDbClientFactory clientFactory) {
        super(clientFactory, GamePlayerRatingChangeLookup.class);
        log.info("initialized GamePlayerRatingChangeLookupDao");
    }
}
