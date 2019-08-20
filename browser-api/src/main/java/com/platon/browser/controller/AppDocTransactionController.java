package com.platon.browser.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.ResponseException;
import com.platon.browser.now.service.TransactionService;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
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

import java.io.IOException;

@RestController
public class AppDocTransactionController implements AppDocTransaction {

	private final Logger logger = LoggerFactory.getLogger(AppDocTransactionController.class);

	@Autowired
	private I18nUtil i18n;

	@Autowired
	private TransactionService transactionService;

	private HttpServletResponse response;
	@ModelAttribute
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	@Override
	public RespPage<TransactionListResp> transactionList(@Valid PageReq req) {
		return transactionService.getTransactionList(req);
	}

	@Override
	public RespPage<TransactionListResp> transactionListByBlock(@Valid TransactionListByBlockRequest req) {
		return transactionService.getTransactionListByBlock(req);
	}

	@Override
	public RespPage<TransactionListResp> transactionListByAddress(@Valid TransactionListByAddressRequest req) {
		return transactionService.getTransactionListByAddress(req);
	}

	@Override
	public void addressTransactionDownload(String address, String date) {
		AccountDownload accountDownload = transactionService.transactionListByAddressDownload(address, date);
		this.download(response, accountDownload.getFilename(), accountDownload.getLength(), accountDownload.getData());
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

	private void download(HttpServletResponse response, String filename, long length, byte [] data){
		response.setHeader("Content-Disposition", "attachment; filename="+filename);
		response.setContentType("application/octet-stream");
		response.setContentLengthLong(length);
		try {
			response.getOutputStream().write(data);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new ResponseException(i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
		}
	}
}
