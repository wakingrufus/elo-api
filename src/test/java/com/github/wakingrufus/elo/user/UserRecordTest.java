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
        // populated object
        User expected = User.builder().build();
        UserRecord input = UserRecord.builder().build();
        User actual = input.toDto();
        Assert.assertEquals("empty object converts correctly", expected, actual);

        // populated object
        String id = UUID.randomUUID().toString();
        String email = "email";
        String name = "name";
        String password = "password";
        expected = User.builder()
                .id(id)
                .email(email)
                .name(name)
                .password(password)
                .build();
        input = UserRecord.builder()
                .id(id)
                .email(email)
                .name(name)
                .password(password)
                .build();
        actual = input.toDto();
        log.info("expected: " + expected.toString());
        log.info("actual: " + actual.toString());
        Assert.assertEquals("populated object converts correctly", expected, actual);
    }

    @Test
    public void testLombok() {
        UserRecord instance = UserRecord.builder().build();
        UserRecord instance2 = instance.toBuilder().build();
        Assert.assertEquals(instance.hashCode(), instance2.hashCode());
        Assert.assertEquals(instance, instance2);
    }

}