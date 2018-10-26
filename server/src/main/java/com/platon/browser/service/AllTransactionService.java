package com.platon.browser.service;

import com.platon.browser.common.dto.transaction.AllTransactionDetail;
import com.platon.browser.common.dto.transaction.AllTransactionList;
import com.platon.browser.common.dto.transaction.TransactionDetail;
import com.platon.browser.common.dto.transaction.TransactionList;
import com.platon.browser.common.req.account.AccountDetailReq;
import com.platon.browser.common.req.transaction.AllTransactionDetailReq;
import com.platon.browser.common.req.transaction.AllTransactionListReq;
import com.platon.browser.common.req.transaction.TransactionDetailReq;
import com.platon.browser.common.req.transaction.TransactionListReq;

import java.util.List;

public interface AllTransactionService {
    List<AllTransactionList> getTransactionList(AllTransactionListReq req);
    List<AllTransactionList> getTransactionList(AccountDetailReq req);
    AllTransactionDetail getTransactionDetail(AllTransactionDetailReq req);
}
