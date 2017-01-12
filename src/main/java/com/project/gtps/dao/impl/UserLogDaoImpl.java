package com.project.gtps.dao.impl;

import com.project.gtps.dao.UserLogDao;
import com.project.gtps.domain.UserLog;

/**
 * Created by suresh on 1/10/17.
 */
public class UserLogDaoImpl extends GenericDaoImpl<UserLog> implements UserLogDao {
    public UserLogDaoImpl() {
        super.setDaoType(UserLog.class);
    }
}
