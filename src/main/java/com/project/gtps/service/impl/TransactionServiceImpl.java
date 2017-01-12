package com.project.gtps.service.impl;

import com.project.gtps.dao.TransactionDao;
import com.project.gtps.dao.impl.TransactionDaoImpl;
import com.project.gtps.domain.Transaction;
import com.project.gtps.service.TransactionService;

import java.util.List;

/**
 * Created by suresh on 1/6/17.
 */
public class TransactionServiceImpl implements TransactionService {

    TransactionDao transactionDao = new TransactionDaoImpl();

    @Override
    public void save(Transaction transaction) {
        transactionDao.save(transaction);
    }

    @Override
    public void findTransactionById(Long tid) {

    }

    @Override
    public List<Transaction> findTxOfUser(String username, String type) {
        return transactionDao.findTxOfUser(username, type);
    }

    @Override
    public void processAcceptRejectTx(Long transactionId,String action) {


        transactionDao.processAcceptRejectTx(transactionId,action);
    }
}
