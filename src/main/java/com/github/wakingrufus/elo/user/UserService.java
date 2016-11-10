package com.github.wakingrufus.elo.user;


import org.jvnet.hk2.annotations.Contract;

@Contract
public interface UserService {

    User getById(String id);

    User getByEmail(String email);
}
