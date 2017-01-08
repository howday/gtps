package com.project.gtps.service.impl;

import com.project.gtps.dao.UserDao;
import com.project.gtps.dao.impl.UserDaoImpl;
import com.project.gtps.domain.Group;
import com.project.gtps.domain.User;
import com.project.gtps.service.UserService;

import java.util.List;

/**
 * Created by suresh on 12/25/16.
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao = new UserDaoImpl();

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public List<User> findAll() {

        return userDao.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public User findOne(Long id) {
        return userDao.findOne(id);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public Boolean validateUser(String userName, String password) {

        return userDao.validate(userName, password);
    }

    @Override
    public List<User> findMatchingUser(String userName) {
        return userDao.findMatchingUser(userName);
    }

    @Override
    public Group findGroupById(Long groupId) {
        return userDao.findGroupById(groupId);
    }


}
