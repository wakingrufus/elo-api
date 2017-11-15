package com.github.wakingrufus.elo.ratinghistory;

import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RatingHistoryService {
    private final GamePlayerRatingChangeDao dao;

    @Inject
    public RatingHistoryService(GamePlayerRatingChangeDao dao) {
        this.dao = dao;
    }

    public RatingHistoryItem create(RatingHistoryItem ratingHistoryItem) {
        RatingHistoryItem toCreate = ratingHistoryItem.toBuilder().id(UUID.randomUUID().toString()).build();
        RatingHistoryItemRecord createdRecord = dao.create(toCreate.toRecord());
        return createdRecord.toDto();
    }

    public List<RatingHistoryItem> getByPlayer(String playerId) {
       return dao.getByPlayer(playerId).stream().map(RatingHistoryItemRecord::toDto).collect(Collectors.toList());
    }

    public void clearHistoryForPlayer(String playerId) {
        dao.getByPlayer(playerId).forEach(dao::delete);
    }
}
