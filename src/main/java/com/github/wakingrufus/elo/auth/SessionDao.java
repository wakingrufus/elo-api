package com.github.wakingrufus.elo.auth;

import org.jvnet.hk2.annotations.Contract;

@Contract
public interface SessionDao {
    SessionRecord findOne(String id);
    void delete(SessionRecord session);
    SessionRecord create(SessionRecord session);
}
