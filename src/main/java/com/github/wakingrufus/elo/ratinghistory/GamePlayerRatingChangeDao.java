package com.github.wakingrufus.elo.ratinghistory;

import com.github.wakingrufus.elo.tech.db.DynamoDbClientFactory;
import com.github.wakingrufus.elo.tech.db.GenericDynamoDbDao;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.List;

@Slf4j
public class GamePlayerRatingChangeDao extends GenericDynamoDbDao<RatingHistoryItemRecord, String> {

    @Inject
    public GamePlayerRatingChangeDao(DynamoDbClientFactory clientFactory) {
        super(clientFactory, RatingHistoryItemRecord.class);
        log.info("initialized GamePlayerRatingChangeDao");
    }

    public List<RatingHistoryItemRecord> getByPlayer(String playerId) {
        return byIndex("RatingHistoryItemByPlayer", RatingHistoryItemRecord.builder().playerId(playerId).build());
    }

}
