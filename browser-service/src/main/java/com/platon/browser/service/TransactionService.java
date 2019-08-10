package com.platon.browser.service;

import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.req.account.AddressDetailReq;
import com.platon.browser.req.transaction.TransactionDetailNavigateReq;
import com.platon.browser.req.transaction.TransactionDetailReq;

import java.util.List;
import java.util.Set;

public interface TransactionService {
    TransactionDetail getDetail(TransactionDetailReq req);
    List<TransactionWithBLOBs> getList( AddressDetailReq req);
    TransactionDetail getTransactionDetailNavigate(TransactionDetailNavigateReq req);
    void clearCache(String chainId);
    void updateCache(String chainId, Set<Transaction> data);
}
