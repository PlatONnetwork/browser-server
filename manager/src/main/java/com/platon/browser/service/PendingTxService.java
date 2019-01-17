package com.platon.browser.service;

import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.transaction.PendingOrTransaction;
import com.platon.browser.dto.transaction.PendingTxItem;
import com.platon.browser.req.account.AddressDetailReq;
import com.platon.browser.req.transaction.PendingTxDetailReq;
import com.platon.browser.req.transaction.PendingTxPageReq;

import java.util.List;

public interface PendingTxService {
    RespPage<PendingTxItem> getTransactionList(PendingTxPageReq req);
    PendingOrTransaction getDetail(PendingTxDetailReq req);
    List<PendingTx> getTransactionList(AddressDetailReq req);
}
