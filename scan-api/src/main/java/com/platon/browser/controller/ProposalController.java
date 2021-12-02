package com.platon.browser.controller;

import com.platon.browser.request.PageReq;
import com.platon.browser.request.proposal.ProposalDetailRequest;
import com.platon.browser.request.proposal.VoteListRequest;
import com.platon.browser.response.BaseResp;
import com.platon.browser.response.RespPage;
import com.platon.browser.response.proposal.ProposalDetailsResp;
import com.platon.browser.response.proposal.ProposalListResp;
import com.platon.browser.response.proposal.VoteListResp;
import com.platon.browser.service.ProposalInfoService;
import com.platon.browser.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 提案模块Contract。定义使用方法
 *
 * @author zhangrj
 * @file AppDocProposalController.java
 * @description
 * @data 2019年8月31日
 */
@Slf4j
@RestController
public class ProposalController {

    @Resource
    private ProposalInfoService proposalInfoService;

    @Resource
    private VoteService voteService;

    /**
     * 提案列表
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.RespPage < com.platon.browser.response.proposal.ProposalListResp>>
     * @date 2021/5/25
     */
    @PostMapping("proposal/proposalList")
    public Mono<RespPage<ProposalListResp>> proposalList(@Valid @RequestBody(required = false) PageReq req) {
        return Mono.just(proposalInfoService.list(req));
    }

    /**
     * 提案详情
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.BaseResp < com.platon.browser.response.proposal.ProposalDetailsResp>>
     * @date 2021/5/25
     */
    @PostMapping("proposal/proposalDetails")
    public Mono<BaseResp<ProposalDetailsResp>> proposalDetails(@Valid @RequestBody ProposalDetailRequest req) {
        return Mono.just(proposalInfoService.get(req));
    }

    /**
     * 投票列表
     *
     * @param req
     * @return reactor.core.publisher.Mono<com.platon.browser.response.RespPage < com.platon.browser.response.proposal.VoteListResp>>
     * @date 2021/5/25
     */
    @PostMapping("proposal/voteList")
    public Mono<RespPage<VoteListResp>> voteList(@Valid @RequestBody VoteListRequest req) {
        return Mono.just(voteService.queryByProposal(req));
    }

}
