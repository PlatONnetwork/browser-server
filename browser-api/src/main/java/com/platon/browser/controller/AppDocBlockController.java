package com.platon.browser.controller;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.Page;
import com.platon.browser.dto.RespPage;
import com.platon.browser.now.service.BlockService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.redis.dto.BlockRedis;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newblock.BlockDetailNavigateReq;
import com.platon.browser.req.newblock.BlockDetailsReq;
import com.platon.browser.req.newblock.BlockListByNodeIdReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.block.BlockDetailResp;
import com.platon.browser.res.block.BlockListResp;

@RestController
public class AppDocBlockController implements AppDocBlock {

	@Autowired
	private BlockService blockService;
	
	@Override
	public RespPage<BlockListResp> blockList(@Valid PageReq req) {
		return blockService.blockList(req);
	}

	@Override
	public RespPage<BlockListResp> blockListByNodeId(@Valid BlockListByNodeIdReq req) {
		RespPage<BlockListResp> respPage = new RespPage<>();
		List<BlockListResp> lists = new LinkedList<>();
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
