package com.platon.browser.service;

import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.req.account.AccountDetailReq;
import com.platon.browser.req.transaction.TransactionDetailNavigateReq;
import com.platon.browser.req.transaction.TransactionDetailReq;
import com.platon.browser.req.transaction.TransactionPageReq;

import java.util.List;
import java.util.Set;

public interface TransactionService {
    RespPage<TransactionListItem> getPage(TransactionPageReq req);
    RespPage<TransactionListItem> getPageByBlockNumber(TransactionPageReq req);
    TransactionDetail getDetail(TransactionDetailReq req);
    List<TransactionWithBLOBs> getList(AccountDetailReq req);
    TransactionDetail getTransactionDetailNavigate(TransactionDetailNavigateReq req);
    void clearCache(String chainId);
    void updateCache(String chainId, Set<Transaction> data);
}
