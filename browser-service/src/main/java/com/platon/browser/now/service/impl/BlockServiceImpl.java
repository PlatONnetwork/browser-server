package com.platon.browser.now.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.BlockExample.Criteria;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.now.service.BlockService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.redis.dto.BlockRedis;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newblock.BlockDetailNavigateReq;
import com.platon.browser.req.newblock.BlockDetailsReq;
import com.platon.browser.req.newblock.BlockListByNodeIdReq;
import com.platon.browser.res.block.BlockDetailResp;
import com.platon.browser.res.block.BlockListResp;

@Service
public class BlockServiceImpl implements BlockService {

	@Autowired
	private StatisticCacheService statisticCacheService;
	
	@Autowired
	private BlockMapper blockMapper;
	
	@Override
	public RespPage<BlockListResp> blockList(@Valid PageReq req) {
		RespPage<BlockListResp> respPage = new RespPage<>();
		List<BlockListResp> lists = new LinkedList<>();
		//小于50万条查询redis
		if(req.getPageNo().intValue() * req.getPageSize().intValue() < 500000) {
			List<BlockRedis> items = statisticCacheService.getBlockCache(req.getPageNo(),req.getPageSize());
			BlockListResp blockListNewResp = new BlockListResp();
			for (int i = 0;i < items.size(); i++) {
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
		List<BlockRedis> blockEnd = statisticCacheService.getBlockCache(1, 1);
		Page<?> page = new Page<>(req.getPageNo(),req.getPageSize());
		page.setTotal(blockEnd.get(0).getNumber());
		respPage.init(page, lists);
		return respPage;
	}

	@Override
	public RespPage<BlockListResp> blockListByNodeId(@Valid BlockListByNodeIdReq req) {
		BlockExample blockExample = new BlockExample();
		blockExample.setOrderByClause("number desc ");
		Criteria criteria = blockExample.createCriteria();
		criteria.andNodeIdEqualTo(req.getNodeId());
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		List<Block> blocks = blockMapper.selectByExample(blockExample);
		RespPage<BlockListResp> respPage = new RespPage<>();
		List<BlockListResp> lists = new LinkedList<>();
		for(Block block : blocks) {
			BlockListResp blockListResp = new BlockListResp();
			blockListResp.setBlockReward(block.getBlockReward());
			blockListResp.setGasUsed(block.getGasUsed());
			blockListResp.setNodeId(block.getNodeId());
			blockListResp.setNodeName(block.getNodeName());
			blockListResp.setNumber(block.getNumber());
			blockListResp.setServerTime(new Date().getTime());
			blockListResp.setSize(block.getSize());
			blockListResp.setStatTxGasLimit(block.getStatTxGasLimit());
			blockListResp.setStatTxQty(block.getStatTxQty());
			blockListResp.setTimestamp(block.getTimestamp().getTime());
			lists.add(blockListResp);
		}
		List<BlockRedis> blockEnd = statisticCacheService.getBlockCache(1, 1);
		Page<?> page = new Page<>(req.getPageNo(),req.getPageSize());
		page.setTotal(blockEnd.get(0).getNumber());
		respPage.init(page, lists);
		return respPage;
	}

	@Override
	public BlockDetailResp blockDetails(BlockDetailsReq req) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BlockDetailResp blockDetailNavigate(BlockDetailNavigateReq req) {
		// TODO Auto-generated method stub
		return null;
	}

}
