package com.platon.browser.now.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.platon.browser.dto.RespPage;
import com.platon.browser.now.service.BlockService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.redis.dto.BlockRedis;
import com.platon.browser.req.PageReq;
import com.platon.browser.res.block.BlockListResp;

@Service
public class BlockServiceImpl implements BlockService {

	@Autowired
	private StatisticCacheService statisticCacheService;
	
	@Override
	public RespPage<BlockListResp> blockList(@Valid PageReq req) {
		RespPage<BlockListResp> respPage = new RespPage<>();
		List<BlockListResp> lists = new LinkedList<>();
		//小于50万条查询redis
		long total = 0l; 
		if(req.getPageNo().intValue() * req.getPageSize().intValue() < 500000) {
			List<BlockRedis> items = statisticCacheService.getBlockCache(req.getPageNo(),req.getPageSize());
			BlockListResp blockListNewResp = new BlockListResp();
			for (int i = 0;i < items.size(); i++) {
				if(i == 0) {
					total = items.get(i).getNumber();
				}
				blockListNewResp.setBlockReward(items.get(i).getBlockReward());
				blockListNewResp.setGasUsed(items.get(i).getGasUsed());
				blockListNewResp.setNodeId(items.get(i).getNodeId());
				blockListNewResp.setNodeName(items.get(i).getNodeName());
				blockListNewResp.setNumber(items.get(i).getNumber());
				blockListNewResp.setServerTime(new Date().getTime());
				blockListNewResp.setSize(items.get(i).getSize());
				blockListNewResp.setStatTxGasLimit(blockListNewResp.getStatTxGasLimit());
				blockListNewResp.setStatTxQty(items.get(i).getStatTxQty());
				blockListNewResp.setTimestamp(items.get(i).getTimestamp().getTime());
				lists.add(blockListNewResp);
			}
		} else {
			//TODO  是否查询超过五十万条数据
		}
		Page<?> page = new Page<>(req.getPageNo(),req.getPageSize());
		page.setTotal(total);
		respPage.init(page, lists);
		return respPage;
	}

}
