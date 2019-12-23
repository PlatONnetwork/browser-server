package com.platon.browser.queue.event;

import com.platon.browser.elasticsearch.dto.Transaction;

import java.util.List;

public class TransactionEvent {
    private List<Transaction> transactionList;

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
}
