package com.project.gtps.dao.impl;

import com.project.gtps.dao.UserDao;
import com.project.gtps.domain.Group;
import com.project.gtps.domain.User;
import com.project.gtps.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserDaoImpl extends GenericDaoImpl<User> implements UserDao {

    public UserDaoImpl() {
        super.setDaoType(User.class);
    }

    /**
     * Find customer based on their email address
     */
    public User findByEmail(String email) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        User user = null;

        try {
            transaction = session.beginTransaction();

            Query query = session.createQuery("from User u where u.email = :email").setParameter("email", email);
            List<User> users = query.list();
            if (users.size() > 0) {
                user = users.get(0);
            }
            transaction.commit();

        } catch (Exception ex) {
            if (transaction != null) transaction.rollback();
            ex.printStackTrace();
        } finally {
//            session.close();
        }
        return user;

    }

    public List<User> findMatchingUser(String user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        List<User> userList = null;


        try {
            transaction = session.beginTransaction();
            userList = session.createQuery("from User u where u.userName like :user").setParameter("user", "%" + user + "%").list();
            transaction.commit();

        } catch (Exception ex) {
            if (transaction != null) transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }

        return userList;
    }

    @Override
    public User findByUsername(String username) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        User user = null;

        try {
            transaction = session.beginTransaction();

            Query query = session.createQuery("from User u where u.userName = :user").setParameter("user", username);
            List<User> users = query.list();
            if (users.size() > 0) {
                user = users.get(0);
            }
            transaction.commit();

        } catch (Exception ex) {
            if (transaction != null) transaction.rollback();
            ex.printStackTrace();
        } finally {
//            session.close();
        }

        return user;
    }

    @Override
    public Boolean validate(String userName, String password) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Boolean isValid = false;

        try {
            transaction = session.beginTransaction();
            String hql = "from User s where s.userName = :userName and password = :password";
            List result = session.createQuery(hql).setParameter("userName", userName).setParameter("password", password).list();
            if (result.size() > 0) {
                isValid = true;
            }
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return isValid;

    }


    @Override
    public Group findGroupById(Long groupId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        Group group = null;

        try {
            transaction = session.beginTransaction();
            group = (Group) session.createQuery("from Group g where g.id = :groupId").setParameter("groupId", groupId).list().get(0);

        } catch (HibernateException e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return group;
    }


}