package com.github.wakingrufus.elo.auth;

import com.github.wakingrufus.elo.user.User;
import com.github.wakingrufus.elo.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Slf4j
@Service
@Singleton
public class DefaultAuthorizationService implements AuthorizationService {
    private final SessionDao sessionDao;
    private final UserService userService;

    @Inject
    public DefaultAuthorizationService(SessionDao sessionDao, UserService userService) {
        log.debug("creating default auth service");
        this.sessionDao = sessionDao;
        this.userService = userService;
        log.debug("sessionDao = " + sessionDao.toString());
        log.debug("userService = " + userService.toString());
    }

    public void logout(String token) {
        sessionDao.delete(sessionDao.findOne(token));
    }

    public Session login(String email, String password) {
        Session session = null;
        User byEmail = userService.getByEmail(email);
        if (byEmail == null) {
            throw new AuthenticationException("user not found with email: " + email);
        }
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(byEmail.getName().getBytes(StandardCharsets.UTF_8));
            String hashedPassword = new String(digest.digest(password.getBytes("UTF-8")));

            if (hashedPassword.equals(byEmail.getPassword())) {
                String token = UUID.randomUUID().toString();
                SessionRecord sessionRecord = new SessionRecord(token, byEmail.getId());
                session = new Session(token, byEmail.getId());
                SessionRecord createdSessionRecord = sessionDao.create(sessionRecord);
                session = createdSessionRecord.toDto();
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return session;
    }

    public User authenticate(String token) {
        log.debug("authenticating user with token: " + token);
        User user = null;
        SessionRecord session = sessionDao.findOne(token);
        if (session != null && session.getUserId() != null) {
            user = userService.getById(session.getUserId());
        } else {
            if (session != null) {
                log.debug("Invalid session: " + session.toString());
            }
        }
        return user;
    }


}
