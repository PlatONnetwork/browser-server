package com.platon.browser.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RestController;

import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.req.transaction.TransactionPageReq;

@RestController
public class AppDocTransactionController implements AppDocTransaction {

	@Override
	public RespPage<TransactionListItem> getPage(@Valid TransactionPageReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
