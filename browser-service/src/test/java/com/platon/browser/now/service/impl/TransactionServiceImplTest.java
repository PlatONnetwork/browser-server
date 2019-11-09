package com.platon.browser.now.service.impl;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.platon.browser.TestBase;
import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.now.service.TransactionService;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newtransaction.TransactionDetailNavigateReq;
import com.platon.browser.req.newtransaction.TransactionDetailsReq;
import com.platon.browser.req.newtransaction.TransactionListByAddressRequest;
import com.platon.browser.req.newtransaction.TransactionListByBlockRequest;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.transaction.TransactionDetailsResp;
import com.platon.browser.res.transaction.TransactionListResp;

public class TransactionServiceImplTest extends TestBase{

	@Autowired
    private TransactionService transactionService;
	
//	@Test
//	public void getDetail() {
//		TransactionDetailReq req = new TransactionDetailReq();
//		req.setTxHash("0x10eab2c84392db35f9caf87c19c183a19f12462c0935a5b9a2f502ef32773d19");
//		TransactionDetail detail = this.transactionService.getDetail(req);
//		assertNotNull(detail);
//	}
	
	@Test
	public void getTransactionList() {
		PageReq req = new PageReq();
		RespPage<TransactionListResp> resp = this.transactionService.getTransactionList(req);
		assertNotNull(resp);
	}
	
	@Test
	public void getTransactionListByBlock() {
		TransactionListByBlockRequest req = new TransactionListByBlockRequest();
		req.setBlockNumber(8546);
		req.setTxType("staking");
		RespPage<TransactionListResp> resp = this.transactionService.getTransactionListByBlock(req);
		assertNotNull(resp);
	}
	
	@Test
	public void getTransactionListByAddress() {
		TransactionListByAddressRequest req = new TransactionListByAddressRequest();
		req.setAddress("0x60ceca9c1290ee56b98d4e160ef0453f7c40d219");
		RespPage<TransactionListResp> resp = this.transactionService.getTransactionListByAddress(req);
		assertNotNull(resp);
	}
	
	@Test
	public void transactionListByAddressDownload() {
		String address = "0x60ceca9c1290ee56b98d4e160ef0453f7c40d219";
		AccountDownload accountDownload = this.transactionService.transactionListByAddressDownload(address, new Date().getTime(), "en", "+8");
		assertNotNull(accountDownload.getData());
	}
	
	@Test
	public void transactionDetails() {
		TransactionDetailsReq req = new TransactionDetailsReq();
		req.setTxHash("0x215eb99a9ee0d0e75e88a99897aa2b71711c09a7296a66591fa88e9c39b13a6f");
		TransactionDetailsResp resp = this.transactionService.transactionDetails(req);
		assertNotNull(resp);
	}
	
}
