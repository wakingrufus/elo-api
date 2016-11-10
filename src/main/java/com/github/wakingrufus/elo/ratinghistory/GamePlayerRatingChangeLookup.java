package com.github.wakingrufus.elo.ratinghistory;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@DynamoDBTable(tableName = "GamePlayerRatingChangeLookup")
public class GamePlayerRatingChangeLookup {
    @DynamoDBRangeKey
    private  String bucket;
    @DynamoDBHashKey
    private  String id;
}
