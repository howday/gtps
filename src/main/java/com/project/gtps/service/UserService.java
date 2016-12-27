package com.project.gtps.service;

import com.project.gtps.domain.User;

import java.util.List;

public interface UserService {

    /**
     * Saves user with set details.
     *
     * @param user
     */
    public void save(User user);

    /**
     * Finds list of all users
     *
     * @return users list
     */
    public List<User> findAll();

    /**
     * Finds user with given email.
     *
     * @param email
     * @return user
     */
    public User findByEmail(String email);

    /**
     * Updates a given user with given set of details.
     *
     * @param user
     */

    public void update(User user);

    /**
     * Deletes a user with given id
     *
     * @param id
     */

    public void delete(Long id);

    /**
     * Finds user with given id.
     *
     * @param id
     * @return user
     */

    public User findOne(Long id);

    public Boolean validateUser(String userName,String password);

    public List<User> findMatchingUser(String userName);

}
