package com.github.wakingrufus.elo.user;


import org.jvnet.hk2.annotations.Contract;

@Contract
public interface CreateUserService {
    User createUser(User user);
}
