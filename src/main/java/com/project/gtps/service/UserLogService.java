package com.project.gtps.service;

import com.project.gtps.domain.UserLog;

import java.util.List;

/**
 * Created by suresh on 1/10/17.
 */
public interface UserLogService {

    public void logActivity(UserLog userLog);

    public List<UserLog> getAllUserLog(String username);

    public List<UserLog> getAllFilteredUserLog();
}
