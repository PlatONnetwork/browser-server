package com.platon.browser.now.service;

import com.platon.browser.dto.transaction.TransactionDetail;
import com.platon.browser.req.transaction.TransactionDetailReq;

import java.util.List;
import java.util.Set;

public interface TransactionService {
	
	public TransactionDetail getDetail(TransactionDetailReq req);
}
