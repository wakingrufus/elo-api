package com.github.wakingrufus.elo.ratinghistory;

import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
public class RatingHistoryService {
    private final GamePlayerRatingChangeDao dao;
    private final GamePlayerRatingChangeLookupDao lookupDao;

    @Inject
    public RatingHistoryService(GamePlayerRatingChangeDao dao, GamePlayerRatingChangeLookupDao lookupDao) {
        this.dao = dao;
        this.lookupDao = lookupDao;
    }

    public RatingHistoryItem create(RatingHistoryItem ratingHistoryItem) {
        RatingHistoryItem toCreate = ratingHistoryItem.toBuilder().id(UUID.randomUUID().toString()).build();
        RatingHistoryItemRecord createdRecord = dao.create(toCreate.toRecord());
        lookupDao.create(GamePlayerRatingChangeLookup.builder().bucket(createdRecord.getPlayerId())
                .id(createdRecord.getId()).build());
        return createdRecord.toDto();
    }

    public List<RatingHistoryItem> getByPlayer(String playerId) {
        Collection<GamePlayerRatingChangeLookup> byPartition = lookupDao.findByPartition(playerId);
        List<RatingHistoryItem> ratingHistoryItemList = new ArrayList<>();
        for (GamePlayerRatingChangeLookup lookup : byPartition) {
            ratingHistoryItemList.add(dao.findOne(lookup.getId()).toDto());
        }
        return ratingHistoryItemList;
    }

    public void clearHistoryForPlayer(String playerId) {
        Collection<GamePlayerRatingChangeLookup> byPartition = lookupDao.findByPartition(playerId);
        for (GamePlayerRatingChangeLookup lookup : byPartition) {
            RatingHistoryItemRecord one = dao.findOne(lookup.getId());
            dao.delete(one);
            lookupDao.delete(lookup);
        }
    }
}
