package com.project.gtps.dao.impl;

import com.project.gtps.dao.UserLogDao;
import com.project.gtps.domain.UserLog;
import com.project.gtps.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by suresh on 1/10/17.
 */
public class UserLogDaoImpl extends GenericDaoImpl<UserLog> implements UserLogDao {
    public UserLogDaoImpl() {
        super.setDaoType(UserLog.class);
    }

    @Override
    public List<UserLog> findAll(String username) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        List<UserLog> userLogList = null;


        try {
            transaction = session.beginTransaction();
            userLogList = session.createQuery("from UserLog u where u.username like :username").setParameter("username", username).list();
            transaction.commit();

        } catch (Exception ex) {
            if (transaction != null) transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }

        return userLogList;
    }
}
