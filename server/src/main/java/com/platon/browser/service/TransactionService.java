package com.platon.browser.service;

import com.platon.browser.common.dto.transaction.TransactionDetail;
import com.platon.browser.common.dto.transaction.TransactionList;
import com.platon.browser.common.req.account.AccountDetailReq;
import com.platon.browser.common.req.transaction.TransactionDetailReq;
import com.platon.browser.common.req.transaction.TransactionListReq;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionWithBLOBs;

import java.util.List;

public interface TransactionService {
    List<TransactionList> getTransactionList(TransactionListReq req);
    TransactionDetail getTransactionDetail(TransactionDetailReq req);

    /**
     * 通过账户信息获取交易列表
     * @param req
     * @return
     */
    List<TransactionWithBLOBs> getTransactionList(AccountDetailReq req);
}
