package com.platon.browser.controller;

import com.platon.browser.common.BrowserConst;
import com.platon.browser.config.CommonMethod;
import com.platon.browser.now.service.ProposalService;
import com.platon.browser.now.service.VoteService;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.proposal.ProposalDetailRequest;
import com.platon.browser.req.proposal.VoteListRequest;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.proposal.ProposalDetailsResp;
import com.platon.browser.res.proposal.ProposalListResp;
import com.platon.browser.res.proposal.VoteListResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.validation.Valid;

/**
 * 提案模块Contract。定义使用方法
 * 
 * @file AppDocProposalController.java
 * @description
 * @author zhangrj
 * @data 2019年8月31日
 */
@RestController
public class AppDocProposalController implements AppDocProposal {
	Logger logger = LoggerFactory.getLogger(AppDocProposalController.class);
	@Autowired
	private ProposalService proposalService;
	@Autowired
	private VoteService voteService;

	@Override
	public WebAsyncTask<RespPage<ProposalListResp>> proposalList(@Valid PageReq req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
		WebAsyncTask<RespPage<ProposalListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT,
				() -> proposalService.list(req));
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}

	@Override
	public WebAsyncTask<BaseResp<ProposalDetailsResp>> proposalDetails(@Valid ProposalDetailRequest req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
		WebAsyncTask<BaseResp<ProposalDetailsResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT,
				() -> proposalService.get(req));
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}

	@Override
	public WebAsyncTask<RespPage<VoteListResp>> voteList(@Valid VoteListRequest req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
		WebAsyncTask<RespPage<VoteListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, () -> voteService.queryByProposal(req));
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}

}
