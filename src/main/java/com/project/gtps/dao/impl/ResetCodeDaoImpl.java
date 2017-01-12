package com.project.gtps.dao.impl;

import com.project.gtps.dao.ResetCodeDao;
import com.project.gtps.domain.ResetCode;
import com.project.gtps.util.Utility;
import com.project.gtps.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;

/**
 * Created by suresh on 1/9/17.
 */
public class ResetCodeDaoImpl extends GenericDaoImpl<ResetCode> implements ResetCodeDao {

    @Override
    public Integer validateResetCode(String resetCode, String email) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;


        try {
            transaction = session.beginTransaction();
            List<ResetCode> rc = session.createQuery("from ResetCode rc where rc.code = :resetCode and rc.email = :email")
                    .setParameter("resetCode", resetCode)
                    .setParameter("email", email)
                    .list();

            if (rc.size() > 0) {
                ResetCode checker = rc.get(0);
                Long timeDifference = Utility.getResetCodeAge(new Date(), checker.getGeneratedDate());
                System.out.println("Time difference: " + timeDifference);
                if (timeDifference > 180000) { //180000 for 3mins
                    return 401;
                }
            } else {
                return 404;
            }

            transaction.commit();

        } catch (Exception ex) {
            if (transaction != null) transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }

        return 200;
    }
}
