package com.github.wakingrufus.elo.tech.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTableMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedList;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.github.wakingrufus.elo.game.GameRecord;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GenericDynamoDbDao<T, K> {
    protected final DynamoDBTableMapper<T, K, ?> tableMapper;

    public GenericDynamoDbDao(DynamoDbClientFactory clientFactory, Class<T> clazz) {
        DynamoDBTableMapper<T, K, ?> tableMapper = new DynamoDBMapper(clientFactory.client()).newTableMapper(clazz);
        log.info("Building table for " + clazz.getName());
        boolean tableIfNotExists = tableMapper.createTableIfNotExists(new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));
        log.info("table created?: " + tableIfNotExists);
        this.tableMapper = tableMapper;
    }

    public T create(T toCreate) {
        tableMapper.saveIfNotExists(toCreate);
        return toCreate;
    }

    public T findOne(K id) {
        return tableMapper.load(id);
    }

    public void delete(T toDelete) {
        this.tableMapper.delete(toDelete);
    }

    public T update(T toUpdate) {
        this.tableMapper.saveIfExists(toUpdate);
        return toUpdate;
    }

    public PaginatedList<T> byIndex(String indexName, T indexValues) {
        if (tableMapper.describeTable().getGlobalSecondaryIndexes().stream().noneMatch(p -> p.getIndexName().equals(indexName))) {
            throw new RuntimeException("no index named: " + indexName);
        }
        PaginatedQueryList<T> results = tableMapper.query(new DynamoDBQueryExpression<T>()
                .withScanIndexForward(true)
                .withLimit(100)
                .withConsistentRead(false)
                .withHashKeyValues(indexValues)
                .withIndexName(indexName));
        results.loadAllResults();
        return results;
    }
}
