package com.github.wakingrufus.elo.user;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

@Slf4j
public class UserRecordTest {
    @Test
    public void toDto() throws Exception {

        // empty object
        UserRecord expected = UserRecord.builder().build();
        User converted = expected.toDto();
        UserRecord actual = converted.toRecord();
        Assert.assertEquals("empty object converts correctly", expected, actual);

        // populated object
        expected = UserRecord.builder()
                .id(UUID.randomUUID().toString())
                .email("email")
                .name("name")
                .password("password")
                .build();
        converted = expected.toDto();
        actual = converted.toRecord();
        Assert.assertEquals("populated object converts correctly", expected, actual);
    }

}