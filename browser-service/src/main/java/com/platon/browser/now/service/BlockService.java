package com.platon.browser.now.service;

import javax.validation.Valid;

import com.platon.browser.dto.RespPage;
import com.platon.browser.req.PageReq;
import com.platon.browser.res.block.BlockListResp;

public interface BlockService {
	
	public RespPage<BlockListResp> blockList(@Valid PageReq req);
	
}
