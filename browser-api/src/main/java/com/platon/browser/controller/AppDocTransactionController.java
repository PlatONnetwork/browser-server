package com.platon.browser.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RestController;

import com.platon.browser.dto.RespPage;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newtransaction.TransactionDetailNavigateReq;
import com.platon.browser.req.newtransaction.TransactionDetailsReq;
import com.platon.browser.req.newtransaction.TransactionListByAddressRequest;
import com.platon.browser.req.newtransaction.TransactionListByBlockRequest;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.transaction.TransactionDetailsResp;
import com.platon.browser.res.transaction.TransactionListResp;

@RestController
public class AppDocTransactionController implements AppDocTransaction {

	@Override
	public RespPage<TransactionListResp> transactionList(@Valid PageReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RespPage<TransactionListResp> transactionListByBlock(@Valid TransactionListByBlockRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RespPage<TransactionListResp> transactionListByAddress(@Valid TransactionListByAddressRequest req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addressTransactionDownload(String address, String date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BaseResp<TransactionDetailsResp> transactionDetails(@Valid TransactionDetailsReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResp<TransactionListResp> transactionDetailNavigate(@Valid TransactionDetailNavigateReq req) {
		// TODO Auto-generated method stub
		return null;
	}

}
