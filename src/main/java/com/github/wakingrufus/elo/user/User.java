package com.github.wakingrufus.elo.user;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class User {
    private final String id;
    private final String name;
    private final String password;
    private final String email;

    UserRecord toRecord() {
        UserRecord ur = UserRecord.builder()
                .name(getName())
                .password(getPassword())
                .email(getEmail())
                .id(getId())
                .build();
        return ur;
    }
}
