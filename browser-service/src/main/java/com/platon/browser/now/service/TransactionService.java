package com.platon.browser.now.service;

import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.account.AddressDetailReq;
import com.platon.browser.req.newtransaction.TransactionListByBlockRequest;
import com.platon.browser.req.transaction.TransactionDetailNavigateReq;
import com.platon.browser.req.transaction.TransactionDetailReq;
import com.platon.browser.res.transaction.TransactionListResp;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

public interface TransactionService {
    TransactionDetail getDetail(TransactionDetailReq req);
    List<TransactionWithBLOBs> getList( AddressDetailReq req);
    TransactionDetail getTransactionDetailNavigate(TransactionDetailNavigateReq req);
    void clearCache(String chainId);
    void updateCache(String chainId, Set<Transaction> data);

    RespPage<TransactionListResp> getTransactionList(@Valid PageReq req);

    RespPage<TransactionListResp> getBlockTransactionList(TransactionListByBlockRequest req);
}
