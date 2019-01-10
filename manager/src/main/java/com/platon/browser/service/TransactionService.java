package com.platon.browser.service;

import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.req.account.AccountDetailReq;
import com.platon.browser.req.transaction.TransactionDetailNavigateReq;
import com.platon.browser.req.transaction.TransactionDetailReq;
import com.platon.browser.req.transaction.TransactionListReq;

import java.util.List;

public interface TransactionService {
    RespPage<TransactionListItem> getPage(TransactionListReq req);
    List<TransactionListItem> getTransactionByBlockNumber(TransactionListReq req);
    TransactionDetail getDetail(TransactionDetailReq req);
    List<TransactionWithBLOBs> getList(AccountDetailReq req);
    TransactionDetail getTransactionDetailNavigate(TransactionDetailNavigateReq req);
}
