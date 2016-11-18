package com.github.wakingrufus.elo.user;

import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
@Singleton
@Slf4j
public class DefaultCreateUserService implements CreateUserService {
    private final UserDao userDao;

    @Inject
    public DefaultCreateUserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User createUser(User user) {
        if (userDao.byEmail(user.getEmail()) != null) {
            throw new UsernameExistsException("account with email [" + user.getEmail() + "] already exists. please choose another.");
        }
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            log.error("hash error: " + e.getLocalizedMessage(), e);
            throw new RuntimeException("Error", e);
        }
        digest.reset();
        digest.update(user.getName().getBytes(StandardCharsets.UTF_8));
        String hashedPassword = null;
        try {
            hashedPassword = new String(digest.digest(user.getPassword().getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        UserRecord newUser = user.toRecord();
        UserRecord enrichedUserRecord = newUser.toBuilder().id(UUID.randomUUID().toString()).password(hashedPassword).build();
        UserRecord createdUserRecord = userDao.create(enrichedUserRecord);
        log.debug("created user record: " + createdUserRecord.toString());
        User createdUser = createdUserRecord.toDto();
        log.debug("created user: " + createdUser.toString());

        // don't send password back
        return createdUser.toBuilder().password(null).build();
    }

}
