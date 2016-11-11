package com.github.wakingrufus.elo.auth;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@DynamoDBTable(tableName = "EloSession")
public class SessionRecord {
    @DynamoDBHashKey
    private String id;
    private String userId;

    Session toDto() {
        return Session.builder().id(getId()).userId(getUserId()).build();
    }
}
