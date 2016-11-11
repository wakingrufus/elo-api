package com.github.wakingrufus.elo.user;

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
@DynamoDBTable(tableName = "EloUser")
public class UserRecord {
    @DynamoDBHashKey
    private String id;
    private String name;
    private String password;
    private String email;

    User toDto() {
        return User.builder().id(getId()).email(getEmail()).name(getName()).password(getPassword()).build();
    }
}
