package com.github.wakingrufus.elo.league;

import com.almworks.sqlite4java.SQLite;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import com.github.wakingrufus.elo.tech.db.DynamoDbClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.UUID;

@Slf4j
public class LeagueDaoTest {
    @Test
    public void testFindOne() throws Exception {
        AWSCredentials credentials = new BasicAWSCredentials("qwe", "qwe");
        AmazonDynamoDB client = new AmazonDynamoDBClient(credentials);

        client.setEndpoint("http://localhost:3210");

        DynamoDbClientFactory clientFactory = Mockito.mock(DynamoDbClientFactory.class);
        Mockito.when(clientFactory.client()).thenReturn(client);


        SQLite.setLibraryPath((this.getClass().getClassLoader().getResource("lib").getPath()));
        final String[] localArgs = {"-inMemory", "-port", "3210"};
        DynamoDBProxyServer server = ServerRunner.createServerFromCommandLineArgs(localArgs);
        server.start();

        log.info("tables: ");
        client.listTables().getTableNames().forEach(log::info);

        UUID uuid = UUID.randomUUID();
        LeagueRecord record = LeagueRecord.builder()
                .id(uuid.toString())
                .name("foosball singles")
                .teamSize(1).build();

        LeagueDao instance = new LeagueDao(clientFactory);
        LeagueRecord created = instance.create(record);
        log.info("created league record:" + created.toString());
        LeagueRecord actual = instance.findOne(uuid.toString());

        Assert.assertEquals(record, actual);
        server.stop();
    }

}