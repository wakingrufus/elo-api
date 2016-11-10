package com.github.wakingrufus.elo.auth;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class Session {
    private final String id;
    private final String userId;

    SessionRecord toDatabase() {
        return SessionRecord.builder().id(getId()).userId(getUserId()).build();
    }
}
