package com.platon.browser.controller;

import com.platon.browser.common.BrowserConst;
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
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;

/**
 *  提案模块Contract。定义使用方法
 *  @file AppDocProposalController.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@RestController
public class AppDocProposalController implements AppDocProposal {
	Logger logger= LoggerFactory.getLogger(AppDocProposalController.class);
    @Autowired
    private ProposalService proposalService;
    @Autowired
    private VoteService voteService;

    @Override
    public WebAsyncTask<RespPage<ProposalListResp>> proposalList(@Valid PageReq req) {
		// 5s钟没返回，则认为超时  
        WebAsyncTask<RespPage<ProposalListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, new Callable<RespPage<ProposalListResp>>() {  
            @Override  
            public RespPage<ProposalListResp> call() throws Exception {  
            	return proposalService.list(req);
            }  
        });  
        webAsyncTask.onCompletion(new Runnable() {  
            @Override  
            public void run() {  
            }  
        });  
        webAsyncTask.onTimeout(new Callable<RespPage<ProposalListResp>>() {  
            @Override  
            public RespPage<ProposalListResp> call() throws Exception {  
                // 超时的时候，直接抛异常，让外层统一处理超时异常  
                throw new TimeoutException("System busy!");  
            }  
        });  
        return webAsyncTask;  
    }

    @Override
    public WebAsyncTask<BaseResp<ProposalDetailsResp>> proposalDetails(@Valid ProposalDetailRequest req) {
    	// 5s钟没返回，则认为超时  
        WebAsyncTask<BaseResp<ProposalDetailsResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, new Callable<BaseResp<ProposalDetailsResp>>() {  
            @Override  
            public BaseResp<ProposalDetailsResp> call() throws Exception {  
            	return proposalService.get(req);
            }  
        });  
        webAsyncTask.onCompletion(new Runnable() {  
            @Override  
            public void run() {  
            }  
        });  
        webAsyncTask.onTimeout(new Callable<BaseResp<ProposalDetailsResp>>() {  
            @Override  
            public BaseResp<ProposalDetailsResp> call() throws Exception {  
                // 超时的时候，直接抛异常，让外层统一处理超时异常  
                throw new TimeoutException("System busy!");  
            }  
        });  
        return webAsyncTask;  
    }

    @Override
    public WebAsyncTask<RespPage<VoteListResp>> voteList(@Valid VoteListRequest req) {
     // 5s钟没返回，则认为超时  
        WebAsyncTask<RespPage<VoteListResp>> webAsyncTask = new WebAsyncTask<>(BrowserConst.WEB_TIME_OUT, new Callable<RespPage<VoteListResp>>() {  
            @Override  
            public RespPage<VoteListResp> call() throws Exception {  
                return voteService.queryByProposal(req);
            }  
        });  
        webAsyncTask.onCompletion(new Runnable() {  
            @Override  
            public void run() {  
            }  
        });  
        webAsyncTask.onTimeout(new Callable<RespPage<VoteListResp>>() {  
            @Override  
            public RespPage<VoteListResp> call() throws Exception {  
                // 超时的时候，直接抛异常，让外层统一处理超时异常  
                throw new TimeoutException("System busy!");  
            }  
        });  
        return webAsyncTask;  
    }

}
