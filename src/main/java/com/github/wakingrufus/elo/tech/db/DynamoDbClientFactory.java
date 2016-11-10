package com.github.wakingrufus.elo.tech.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.github.wakingrufus.elo.config.AppConfig;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;

@Service
public class DynamoDbClientFactory {
    private final AppConfig appConfig;
    private AmazonDynamoDB client;

    @Inject
    public DynamoDbClientFactory(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    public AmazonDynamoDB client() {
        if (client == null) {
            client = new AmazonDynamoDBClient();
            if (appConfig.getDbUrl() != null) {
                client.setEndpoint(appConfig.getDbUrl());
            }
        }
        return client;
    }
}
