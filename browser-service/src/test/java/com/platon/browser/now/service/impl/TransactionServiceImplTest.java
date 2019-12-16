package com.platon.browser.now.service.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.platon.browser.TestBase;
import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.now.service.TransactionService;
import com.platon.browser.req.PageReq;
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
		AccountDownload accountDownload = this.transactionService.transactionListByAddressDownload(address, 1560099928000l, "en", "+8");
		assertNotNull(accountDownload.getData());
	}
	
	@Test
	public void transactionDetails() {
		TransactionDetailsReq req = new TransactionDetailsReq();
		req.setTxHash("0xc0e9ab13065776c46c59f87f77a46cd7ec689b13d1138514c7f467eca582b88b");
		TransactionDetailsResp resp = this.transactionService.transactionDetails(req);
		assertNotNull(resp);
		req.setTxHash("0x529cddffab0b0a3b4c2c6df10a9fcbaa452d3ac20e987f9e5a1b11f5b15c3972");
		TransactionDetailsResp resp1 = this.transactionService.transactionDetails(req);
		assertNotNull(resp1);
		req.setTxHash("0xf051fcbe77b8d39d4e5489069d2f044179f70afe30f4fe4537adc97217121d1a");
		TransactionDetailsResp resp2 = this.transactionService.transactionDetails(req);
		assertNotNull(resp2);
		req.setTxHash("0x90fc7fdf0327c99d64552c34569dc9440a6301bb08b5185c7a35cb101fe2c5a3");
		TransactionDetailsResp resp3 = this.transactionService.transactionDetails(req);
		assertNotNull(resp3);
		req.setTxHash("0xa1a222056c20a300da303966381a6e6175f3375c6ef997a5be3dcdbd2787f145");
		TransactionDetailsResp resp4 = this.transactionService.transactionDetails(req);
		assertNotNull(resp4);
		
	}
	
}
