package com.platon.browser.now.service;

import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.req.transaction.TransactionDetailReq;

public interface TransactionService {
	
	public TransactionDetail getDetail(TransactionDetailReq req);
}
