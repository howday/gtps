package com.project.gtps.dao.impl;

import com.project.gtps.dao.TransactionDao;
import com.project.gtps.domain.Device;
import com.project.gtps.domain.Transaction;
import com.project.gtps.domain.User;
import com.project.gtps.domain.UserLog;
import com.project.gtps.service.UserLogService;
import com.project.gtps.service.UserService;
import com.project.gtps.service.impl.UserLogServiceImpl;
import com.project.gtps.service.impl.UserServiceImpl;
import com.project.gtps.util.HibernateUtil;
import com.project.gtps.util.Notification;
import org.hibernate.Session;

import java.util.Date;
import java.util.List;

/**
 * Created by suresh on 1/6/17.
 */
public class TransactionDaoImpl extends GenericDaoImpl<Transaction> implements TransactionDao {

    @Override
    public Transaction findTransactionById(Long tid) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        try {
            return (Transaction) session.createQuery("from Transaction u where u.id = :id").setParameter("id", tid).list().get(0);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Transaction> findTxOfUser(String username, String type) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Transaction> transactionList = null;
        org.hibernate.Transaction transaction = null;

        try {

            transaction = session.beginTransaction();
            if (type.equalsIgnoreCase("completed")) {
                transactionList = session.createQuery("from Transaction t where t.requestFromId = :user and t.status != :status order by t.date desc")
                        .setParameter("user", username)
                        .setParameter("status", "PENDING")
                        .list();

            } else {
                transactionList = session.createQuery("from Transaction t where t.requestToId = :user and t.status = :status order by t.date desc")
                        .setParameter("user", username)
                        .setParameter("status", "PENDING")
                        .list();
            }

        } catch (Exception ex) {
            if (transaction != null) transaction.rollback();
            ex.printStackTrace();
        } finally {
            session.close();
        }

        return transactionList;
    }

    @Override

    public void processAcceptRejectTx(Long transactionId, String action) {

        UserLogService userLogService = new UserLogServiceImpl();
        UserService userService = new UserServiceImpl();
        Transaction tx = findTransactionById(transactionId);
        System.out.println(tx.getDescription());

        String response = "rejected";
        if (action.equalsIgnoreCase("ACCEPT")) {
            response = "accepted";

            /**
             * Update the status and date of pending transaction
             */
            tx.setStatus("RECEIVED");
            tx.setDate(new Date());
            update(tx);
            /**
             * Enter new transaction for sender to record as SENT.
             */
            Transaction newTransaction = new Transaction();
            newTransaction.setAmount(tx.getAmount());
            newTransaction.setDate(new Date());
            newTransaction.setDescription(tx.getDescription());
            newTransaction.setRequestFromId(tx.getRequestToId());
            newTransaction.setRequestToId(tx.getRequestFromId());
            newTransaction.setStatus("SENT");
            save(newTransaction);

        } else {
            /**
             * Update the status and date of pending transaction
             */
            tx.setStatus("REJECTED");
            tx.setDate(new Date());
            update(tx);
        }

        String mess = String.format("%s has %s your request of %f for %s. [%s]",
                tx.getRequestToId(), response, tx.getAmount(), tx.getDescription(), new Date());
        UserLog userLog = new UserLog();
        userLog.setUsername(tx.getRequestFromId());
        userLog.setDescription(mess);
        userLog.setGeneratedDate(new Date());
        userLogService.logActivity(userLog);

        /**
         * Sends notification to user that their request has been responded
         */
        String messageToDevice = String.format("%s::2", mess, userLog.getGeneratedDate());
        System.out.println("Message to device: " + messageToDevice);
        User user = userService.findByUsername(tx.getRequestFromId());
        Device device = user.getDevices().get(0);
        Notification.send(messageToDevice, device.getDeviceId());


    }
}
