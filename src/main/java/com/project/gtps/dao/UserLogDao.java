package com.project.gtps.dao;

import com.project.gtps.domain.UserLog;

import java.util.List;

/**
 * Created by suresh on 1/10/17.
 */
public interface UserLogDao extends GenericDao<UserLog> {

    public List<UserLog> findAll(String username);
}
