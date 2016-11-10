package com.github.wakingrufus.elo.tech.db;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTableMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
public class GenericDynamoDbRangeDao<T, K, R> {
    protected final DynamoDBTableMapper<T, K, R> tableMapper;

    public GenericDynamoDbRangeDao(DynamoDbClientFactory clientFactory, Class<T> clazz) {
        DynamoDBTableMapper<T, K, R> tableMapper = new DynamoDBMapper(clientFactory.client()).newTableMapper(clazz);
        log.info("Building table for " + clazz.getName());
        tableMapper.deleteTableIfExists();
        tableMapper.createTableIfNotExists(new ProvisionedThroughput().withReadCapacityUnits(1L).withWriteCapacityUnits(1L));
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

    public Collection<T> findByPartition(String bucketName) {
        PaginatedQueryList<T> result = this.tableMapper.query(
                new DynamoDBQueryExpression<T>().withRangeKeyCondition(tableMapper.rangeKey().name(),
                        new Condition()
                                .withComparisonOperator(ComparisonOperator.EQ)
                                .withAttributeValueList(new AttributeValue(bucketName))));
        return result;
    }
}
