package com.project.gtps.service.impl;

import com.project.gtps.dao.UserLogDao;
import com.project.gtps.dao.impl.UserLogDaoImpl;
import com.project.gtps.domain.UserLog;
import com.project.gtps.service.UserLogService;

import java.util.List;

/**
 * Created by suresh on 1/10/17.
 */
public class UserLogServiceImpl implements UserLogService {

    UserLogDao userLogDao = new UserLogDaoImpl();

    @Override
    public void logActivity(UserLog userLog) {
        userLogDao.save(userLog);
    }

    @Override
    public List<UserLog> getAllUserLog(String username) {

        return userLogDao.findAll(username);
    }

    @Override
    public List<UserLog> getAllFilteredUserLog() {
        return userLogDao.findAll();
    }
}
