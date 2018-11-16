package com.platon.browser.service;

import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dto.transaction.PendingOrTransaction;
import com.platon.browser.dto.transaction.PendingTxDetailNavigate;
import com.platon.browser.dto.transaction.PendingTxItem;
import com.platon.browser.req.account.AccountDetailReq;
import com.platon.browser.req.transaction.PendingTxDetailNavigateReq;
import com.platon.browser.req.transaction.PendingTxDetailReq;
import com.platon.browser.req.transaction.PendingTxListReq;

import java.util.List;

public interface PendingTxService {
    List<PendingTxItem> getTransactionList(PendingTxListReq req);
    PendingOrTransaction getTransactionDetail(PendingTxDetailReq req);
    List<PendingTx> getTransactionList(AccountDetailReq req);
    PendingTxDetailNavigate getPendingTxDetailNavigate(PendingTxDetailNavigateReq req);
}
