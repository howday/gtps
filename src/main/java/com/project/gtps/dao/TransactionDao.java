package com.project.gtps.dao;

import com.project.gtps.domain.Transaction;

import java.util.List;

/**
 * Created by suresh on 1/6/17.
 */
public interface TransactionDao extends GenericDao<Transaction> {

    public Transaction findTransactionById(Long tid);

    public List<Transaction> findTxOfUser(String username, String type);

    public void processAcceptRejectTx(Long transactionId, String action);


}
