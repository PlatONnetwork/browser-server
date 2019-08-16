package com.platon.browser.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.platon.browser.dto.RespPage;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.now.service.BlockService;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newblock.BlockDetailNavigateReq;
import com.platon.browser.req.newblock.BlockDetailsReq;
import com.platon.browser.req.newblock.BlockListByNodeIdReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.block.BlockDetailResp;
import com.platon.browser.res.block.BlockListResp;
import com.platon.browser.util.I18nUtil;

@RestController
public class AppDocBlockController implements AppDocBlock {

	@Autowired
	private I18nUtil i18n;
	
	@Autowired
	private BlockService blockService;
	
	@Override
	public RespPage<BlockListResp> blockList(@Valid PageReq req) {
		return blockService.blockList(req);
	}

	@Override
	public RespPage<BlockListResp> blockListByNodeId(@Valid BlockListByNodeIdReq req) {
		return blockService.blockListByNodeId(req);
	}

	@Override
	public void blockListByNodeIdDownload(String nodeId, String date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BaseResp<BlockDetailResp> blockDetails(@Valid BlockDetailsReq req) {
		BlockDetailResp blockDetailResp = blockService.blockDetails(req);
		return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),blockDetailResp);
	}

	@Override
	public BaseResp<BlockDetailResp> blockDetailNavigate(@Valid BlockDetailNavigateReq req) {
		BlockDetailResp blockDetailResp = blockService.blockDetailNavigate(req);
		return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),blockDetailResp);
	}

}
