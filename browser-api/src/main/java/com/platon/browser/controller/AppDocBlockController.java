package com.platon.browser.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RestController;

import com.platon.browser.dto.RespPage;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newblock.BlockDetailNavigateReq;
import com.platon.browser.req.newblock.BlockDetailsReq;
import com.platon.browser.req.newblock.BlockListByNodeIdReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.block.BlockDetailResp;
import com.platon.browser.res.block.BlockListResp;

@RestController
public class AppDocBlockController implements AppDocBlock {

	@Override
	public RespPage<BlockListResp> blockList(@Valid PageReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public RespPage<BlockListResp> blockListByNodeId(@Valid BlockListByNodeIdReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void blockListByNodeIdDownload(String nodeId, String date) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public BaseResp<BlockDetailResp> blockDetails(@Valid BlockDetailsReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseResp<BlockDetailResp> blockDetailNavigate(@Valid BlockDetailNavigateReq req) {
		// TODO Auto-generated method stub
		return null;
	}

}
