package com.platon.browser.now.service;

import com.platon.browser.req.PageReq;
import com.platon.browser.req.newblock.BlockDetailNavigateReq;
import com.platon.browser.req.newblock.BlockDetailsReq;
import com.platon.browser.req.newblock.BlockDownload;
import com.platon.browser.req.newblock.BlockListByNodeIdReq;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.block.BlockDetailResp;
import com.platon.browser.res.block.BlockListResp;

/**
 *  区块服务接口
 *  @file BlockService.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public interface BlockService {
	
	/**
	 *  分页查询区块列表
	 * @method blockList
	 * @param req
	 * @return
	 */
	public RespPage<BlockListResp> blockList( PageReq req);
	
	/**
	 *  根据节点id分页查询区块列表
	 * @method blockListByNodeId
	 * @param req
	 * @return
	 */
	public RespPage<BlockListResp> blockListByNodeId( BlockListByNodeIdReq req);
	
	/**
	 * 根据区块id查询区块详情
	 * @method blockDetails
	 * @param req
	 * @return
	 */
	public BlockDetailResp blockDetails( BlockDetailsReq req);
	
	/**
	 * 根据区块id切换上一个下一个区块详情
	 * @method blockDetailNavigate
	 * @param req
	 * @return
	 */
	public BlockDetailResp blockDetailNavigate( BlockDetailNavigateReq req);
	
	/**
	 * 根据节点id和开始日期下载文件
	 * @method blockListByNodeIdDownload
	 * @param nodeId
	 * @param date
	 * @return
	 */
	public BlockDownload blockListByNodeIdDownload(String nodeId, Long date, String local, String timeZone);
}
