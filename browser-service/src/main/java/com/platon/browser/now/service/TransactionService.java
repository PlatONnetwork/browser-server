package com.platon.browser.now.service;

import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newtransaction.TransactionDetailNavigateReq;
import com.platon.browser.req.newtransaction.TransactionDetailsReq;
import com.platon.browser.req.newtransaction.TransactionListByAddressRequest;
import com.platon.browser.req.newtransaction.TransactionListByBlockRequest;
import com.platon.browser.res.transaction.TransactionDetailsResp;
import com.platon.browser.res.transaction.TransactionListResp;

public interface TransactionService {
//    public TransactionDetail getDetail(TransactionDetailReq req);

    RespPage<TransactionListResp> getTransactionList( PageReq req);

    RespPage<TransactionListResp> getTransactionListByBlock(TransactionListByBlockRequest req);

    RespPage<TransactionListResp> getTransactionListByAddress(TransactionListByAddressRequest req);

    AccountDownload transactionListByAddressDownload(String address, String date);

	TransactionDetailsResp transactionDetails( TransactionDetailsReq req);

	TransactionListResp transactionDetailNavigate( TransactionDetailNavigateReq req);
}
