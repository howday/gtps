package com.project.gtps.dao;

import com.project.gtps.domain.User;

import java.util.List;

public interface UserDao extends GenericDao<User> {

    /**
     * Finds user based on their email address provided.
     *
     * @param email
     * @return customer
     */
    public User findByEmail(String email);

    public Boolean validate(String userName, String password);

    public List<User> findMatchingUser(String email);

}
