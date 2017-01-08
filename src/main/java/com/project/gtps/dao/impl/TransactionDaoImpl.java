package com.project.gtps.dao.impl;

import com.project.gtps.dao.TransactionDao;
import com.project.gtps.domain.Transaction;
import com.project.gtps.util.HibernateUtil;
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

    public void processAcceptRejectTx(Long transactionId) {
        Transaction tx = findTransactionById(transactionId);
        System.out.println(tx.getDescription());

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

    }
}
