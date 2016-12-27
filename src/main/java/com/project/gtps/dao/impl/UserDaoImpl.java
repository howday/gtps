package com.project.gtps.dao.impl;

import com.project.gtps.dao.UserDao;
import com.project.gtps.domain.User;
import com.project.gtps.util.HibernateUtil;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.List;

public class UserDaoImpl extends GenericDaoImpl<User> implements UserDao {

    public UserDaoImpl() {
        super.setDaoType(User.class);
    }

    /**
     * Find customer based on their email address
     */
    public User findByEmail(String email) {

        Query query = entityManager.createQuery("select u from User u  where u.email =:email");
        return (User) query.setParameter("email", email).getSingleResult();

    }

    public List<User> findMatchingUser(String user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            return session.createQuery("from User u where u.userName like :user").setParameter("user", "%" + user + "%").list();

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean validate(String userName, String password) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        System.out.println(userName);
        System.out.println(password);
        try {
            String hql = "from User s where s.userName = :userName and password = :password";
            List result = session.createQuery(hql).setParameter("userName", userName).setParameter("password", password).list();
            if (result.size() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }


    }


}