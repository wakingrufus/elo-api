package com.github.wakingrufus.elo.player;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

@Slf4j
public class PlayerTest {
    @Test
    public void toRecord() throws Exception {
        // test empty object
        PlayerRecord expected = PlayerRecord.builder().build();
        Player input = Player.builder().build();
        PlayerRecord actual = input.toRecord();

        log.info("expected: " + expected.toString());
        log.info("actual:   " + actual.toString());
        Assert.assertEquals("converts empty object correctly", expected, actual);
    }

    @Test
    public void testSerialization() {

    }
}