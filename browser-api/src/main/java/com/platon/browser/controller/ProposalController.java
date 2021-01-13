package com.platon.browser.controller;

import com.platon.browser.constant.Browser;
import com.platon.browser.config.CommonMethod;
import com.platon.browser.service.ProposalService;
import com.platon.browser.service.VoteService;
import com.platon.browser.request.PageReq;
import com.platon.browser.request.proposal.ProposalDetailRequest;
import com.platon.browser.request.proposal.VoteListRequest;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.proposal.ProposalDetailsResp;
import com.platon.browser.response.proposal.ProposalListResp;
import com.platon.browser.response.proposal.VoteListResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.annotation.Resource;
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
public class ProposalController {
	Logger logger = LoggerFactory.getLogger(ProposalController.class);
	@Resource
	private ProposalService proposalService;
	@Resource
	private VoteService voteService;

	@PostMapping(value = "proposal/proposalList")
	public WebAsyncTask<RespPage<ProposalListResp>> proposalList(@Valid @RequestBody(required = false) PageReq req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
		WebAsyncTask<RespPage<ProposalListResp>> webAsyncTask = new WebAsyncTask<>(Browser.WEB_TIME_OUT,
				() -> proposalService.list(req));
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}

	@PostMapping(value = "proposal/proposalDetails")
	public WebAsyncTask<BaseResp<ProposalDetailsResp>> proposalDetails(@Valid @RequestBody ProposalDetailRequest req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
		WebAsyncTask<BaseResp<ProposalDetailsResp>> webAsyncTask = new WebAsyncTask<>(Browser.WEB_TIME_OUT,
				() -> proposalService.get(req));
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}

	@PostMapping(value = "proposal/voteList")
	public WebAsyncTask<RespPage<VoteListResp>> voteList(@Valid @RequestBody VoteListRequest req) {
		/**
		 * 异步调用，超时则进入timeout  
		 */
		WebAsyncTask<RespPage<VoteListResp>> webAsyncTask = new WebAsyncTask<>(Browser.WEB_TIME_OUT, () -> voteService.queryByProposal(req));
		CommonMethod.onTimeOut(webAsyncTask);
		return webAsyncTask;
	}
}
