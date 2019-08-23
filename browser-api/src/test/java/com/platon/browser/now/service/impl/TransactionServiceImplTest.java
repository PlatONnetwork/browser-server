package com.platon.browser.now.service.impl;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.platon.browser.BrowserApiApplication;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.now.service.TransactionService;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newtransaction.TransactionDetailNavigateReq;
import com.platon.browser.req.newtransaction.TransactionDetailsReq;
import com.platon.browser.req.newtransaction.TransactionListByAddressRequest;
import com.platon.browser.req.newtransaction.TransactionListByBlockRequest;
import com.platon.browser.req.transaction.TransactionDetailReq;
import com.platon.browser.res.transaction.TransactionDetailsResp;
import com.platon.browser.res.transaction.TransactionListResp;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BrowserApiApplication.class)
public class TransactionServiceImplTest {

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
		req.setBlockNumber(56779);
		req.setTxType("0");
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
		String date = "2019-08-21";
		// TODO
		AccountDownload accountDownload = this.transactionService.transactionListByAddressDownload(address, date);
		assertNotNull(accountDownload.getData());
	}
	
	@Test
	public void transactionDetails() {
		TransactionDetailsReq req = new TransactionDetailsReq();
		req.setTxHash("0x215eb99a9ee0d0e75e88a99897aa2b71711c09a7296a66591fa88e9c39b13a6f");
		TransactionDetailsResp resp = this.transactionService.transactionDetails(req);
		assertNotNull(resp);
	}
	
	@Test
	public void transactionDetailNavigate() {
		TransactionDetailNavigateReq req = new TransactionDetailNavigateReq();
		req.setTxHash("0x215eb99a9ee0d0e75e88a99897aa2b71711c09a7296a66591fa88e9c39b13a6f");
		req.setDirection("prev");
		TransactionListResp resp = this.transactionService.transactionDetailNavigate(req);
		assertNotNull(resp);
	}
	
}
