package com.github.wakingrufus.elo.league;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wakingrufus.elo.tech.ObjectMapperFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class GameTypeTest {

    @Test
    public void testSerialization() throws IOException {
        ObjectMapper objectMapper = new ObjectMapperFactory().buildObjectMapper();
        String json = objectMapper.writeValueAsString(GameType.FOOSBALL);
        GameType actual = objectMapper.readValue(json, GameType.class);
        Assert.assertEquals(GameType.FOOSBALL, actual);

        List<GameType> types = new ArrayList<>();
        types.add(GameType.CHESS);
        types.add(GameType.FOOSBALL);
        json = objectMapper.writeValueAsString(types);
        GameType[] actualArr = objectMapper.readValue(json, GameType[].class);
        List<GameType> actualList = Arrays.asList(actualArr);
        Assert.assertEquals(GameType.FOOSBALL, actualList.get(1));
    }
}