package com.platon.browser.service;

import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.req.account.AccountDetailReq;
import com.platon.browser.req.transaction.TransactionDetailNavigateReq;
import com.platon.browser.req.transaction.TransactionDetailReq;
import com.platon.browser.req.transaction.TransactionListReq;

import java.util.List;

public interface TransactionService {
    List<TransactionListItem> getTransactionByBlockNumber(TransactionListReq req);
    TransactionDetail getTransactionDetail(TransactionDetailReq req);
    List<TransactionWithBLOBs> getTransactionList(AccountDetailReq req);
    TransactionDetail getTransactionDetailNavigate(TransactionDetailNavigateReq req);
}
