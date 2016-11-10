package com.github.wakingrufus.elo.auth;

import com.github.wakingrufus.elo.user.User;
import org.jvnet.hk2.annotations.Contract;

@Contract
public interface AuthorizationService {

    void logout(String token);

    Session login(String email, String password);

    User authenticate(String token);

}
