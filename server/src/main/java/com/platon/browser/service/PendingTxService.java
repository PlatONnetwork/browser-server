package com.platon.browser.service;

import com.platon.browser.common.dto.transaction.PendingTxDetail;
import com.platon.browser.common.dto.transaction.PendingTxList;
import com.platon.browser.common.dto.transaction.TransactionDetail;
import com.platon.browser.common.dto.transaction.TransactionList;
import com.platon.browser.common.req.account.AccountDetailReq;
import com.platon.browser.common.req.transaction.PendingTxDetailReq;
import com.platon.browser.common.req.transaction.PendingTxListReq;
import com.platon.browser.common.req.transaction.TransactionDetailReq;
import com.platon.browser.common.req.transaction.TransactionListReq;
import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dao.entity.Transaction;

import java.util.List;

public interface PendingTxService {
    List<PendingTxList> getTransactionList(PendingTxListReq req);
    PendingTxDetail getTransactionDetail(PendingTxDetailReq req);

    /**
     * 通过账户信息获取待处理交易列表
     * @param req
     * @return
     */
    List<PendingTx> getTransactionList(AccountDetailReq req);
}
