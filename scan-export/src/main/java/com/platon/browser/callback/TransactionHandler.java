package com.platon.browser.callback;

import com.platon.browser.elasticsearch.dto.Transaction;

public interface TransactionHandler {
    void handle(Transaction tx);
}
