package com.project.gtps.service;

import com.project.gtps.domain.Transaction;

import java.util.List;

/**
 * Created by suresh on 1/6/17.
 */
public interface TransactionService {


    /**
     * Saves user with set details.
     *
     * @param user
     */
    public void save(Transaction user);

    public void findTransactionById(Long tid);


    public List<Transaction> findTxOfUser(String username, String type);

    public void processAcceptRejectTx(Long transactionId);

}
