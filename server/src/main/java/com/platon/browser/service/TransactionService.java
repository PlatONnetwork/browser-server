package com.platon.browser.service;

import com.platon.browser.common.dto.transaction.TransactionDetail;
import com.platon.browser.common.dto.transaction.TransactionList;
import com.platon.browser.common.req.transaction.TransactionDetailReq;
import com.platon.browser.common.req.transaction.TransactionListReq;

import java.util.List;

public interface TransactionService {
    List<TransactionList> getTransactionList(TransactionListReq req);
    TransactionDetail getTransactionDetail(TransactionDetailReq req);
}
