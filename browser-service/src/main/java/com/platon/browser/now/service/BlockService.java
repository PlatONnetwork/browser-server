package com.platon.browser.now.service;

import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockDownload;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newblock.BlockDetailNavigateReq;
import com.platon.browser.req.newblock.BlockDetailsReq;
import com.platon.browser.req.newblock.BlockListByNodeIdReq;
import com.platon.browser.res.block.BlockDetailResp;
import com.platon.browser.res.block.BlockListResp;

public interface BlockService {
	
	public RespPage<BlockListResp> blockList( PageReq req);
	
	public RespPage<BlockListResp> blockListByNodeId( BlockListByNodeIdReq req);
	
	public BlockDetailResp blockDetails( BlockDetailsReq req);
	
	public BlockDetailResp blockDetailNavigate( BlockDetailNavigateReq req);
	
	public BlockDownload blockListByNodeIdDownload(String nodeId, String date);
}
