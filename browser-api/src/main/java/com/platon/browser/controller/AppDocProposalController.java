package com.platon.browser.controller;

import javax.validation.Valid;

import com.alibaba.fastjson.JSONObject;
import com.platon.browser.dto.RespPage;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.now.service.ProposalService;
import com.platon.browser.now.service.VoteService;
import com.platon.browser.util.I18nUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.platon.browser.req.PageReq;
import com.platon.browser.req.proposal.ProposalDetailRequest;
import com.platon.browser.req.proposal.VoteListRequest;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.proposal.ProposalDetailsResp;
import com.platon.browser.res.proposal.ProposalListResp;
import com.platon.browser.res.proposal.VoteListResp;

import java.util.Objects;

@RestController
public class AppDocProposalController implements AppDocProposal {
	Logger logger= LoggerFactory.getLogger(AppDocProposalController.class);
	@Autowired
	private I18nUtil i18n;
    @Autowired
    private ProposalService proposalService;
    @Autowired
    private VoteService voteService;

    @Override
    public RespPage<ProposalListResp> proposalList(@Valid PageReq req) {
        return proposalService.list(req);
    }

    @Override
    public BaseResp<ProposalDetailsResp> proposalDetails(@Valid ProposalDetailRequest req) {
        return proposalService.get(req);
    }

    @Override
    public RespPage<VoteListResp> voteList(@Valid VoteListRequest req) {
    	if(Objects.isNull(req)|| StringUtils.isBlank(req.getProposalHash())){
    		logger.error("## ERROR # proposal param error req:{}", JSONObject.toJSONString(req));
    		throw new BusinessException(RetEnum.RET_PARAM_VALLID.getCode(),i18n.i(I18nEnum.PROPOSAL_PARAM_ERROR));
		}
        return voteService.queryByProposal(req);
    }

}
