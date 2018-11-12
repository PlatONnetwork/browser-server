package com.platon.browser.service;

import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dto.transaction.PendingOrTransaction;
import com.platon.browser.dto.transaction.PendingTxDetailNavigate;
import com.platon.browser.dto.transaction.PendingTxItem;
import com.platon.browser.req.account.AccountDetailReq;
import com.platon.browser.req.account.ContractDetailReq;
import com.platon.browser.req.transaction.PendingTxDetailNavigateReq;
import com.platon.browser.req.transaction.PendingTxDetailReq;
import com.platon.browser.req.transaction.PendingTxListReq;

import java.util.List;

public interface PendingTxService {
    List<PendingTxItem> getTransactionList(PendingTxListReq req);
    PendingOrTransaction getTransactionDetail(PendingTxDetailReq req);

    /**
     * 通过账户信息获取待处理交易列表
     * @param req
     * @return
     */
    List<PendingTx> getTransactionList(AccountDetailReq req);

    List<PendingTx> getContractList(ContractDetailReq req);

    PendingTxDetailNavigate getPendingTxDetailNavigate(PendingTxDetailNavigateReq req);
}
