package com.github.wakingrufus.elo.user;


import lombok.extern.slf4j.Slf4j;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.inject.Singleton;

@Service
@Singleton
@Slf4j
public class DefaultUserService implements UserService {
    private final UserDao userDao;

    @Inject
    public DefaultUserService(UserDao userDao) {
        log.debug("creating user service");
        this.userDao = userDao;
        log.debug("userDao = " + userDao.toString());
    }


    public User getById(String id) {
        UserRecord userRecord = userDao.findOne(id);
        User user = null;
        if (userRecord != null) {
            user = userRecord.toDto();
        }
        return user;
    }

    @Override
    public User getByEmail(String email) {
        User user = null;
        UserRecord userRecord = null;
        UserRecord lookup = userDao.byEmail(email);
        if (lookup != null) {
            userRecord = userDao.findOne(lookup.getId());
        }
        if (userRecord != null) {
            user = userRecord.toDto();
        }
        return user;
    }

}
