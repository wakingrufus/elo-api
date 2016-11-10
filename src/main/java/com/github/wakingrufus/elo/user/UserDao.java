package com.github.wakingrufus.elo.user;

import org.jvnet.hk2.annotations.Contract;

@Contract
public interface UserDao {
    UserRecord findOne(String id);
    UserRecord create(UserRecord user);
}
