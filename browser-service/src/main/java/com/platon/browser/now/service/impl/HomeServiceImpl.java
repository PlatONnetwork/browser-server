package com.platon.browser.now.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.platon.browser.dao.mapper.BlockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingExample;
import com.platon.browser.dao.entity.StakingExample.Criteria;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.IsConsensusStatus;
import com.platon.browser.enums.StakingStatus;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.now.service.HomeService;
import com.platon.browser.now.service.TransactionService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.redis.dto.BlockRedis;
import com.platon.browser.redis.dto.NetworkStatRedis;
import com.platon.browser.req.home.QueryNavigationRequest;
import com.platon.browser.req.transaction.TransactionDetailReq;
import com.platon.browser.res.home.BlockListNewResp;
import com.platon.browser.res.home.BlockStatisticNewResp;
import com.platon.browser.res.home.ChainStatisticNewResp;
import com.platon.browser.res.home.QueryNavigationResp;
import com.platon.browser.res.home.QueryNavigationStructResp;
import com.platon.browser.res.home.StakingListNewResp;
import com.platon.browser.util.I18nUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HomeServiceImpl implements HomeService {

	@Autowired
	private BlockMapper blockMapper;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private StatisticCacheService statisticCacheService;
	@Autowired
	private I18nUtil i18n;
	@Autowired
	private StakingMapper stakingMapper;

	@Override
	public QueryNavigationResp queryNavigation(QueryNavigationRequest req) {
		// 以太坊内部和外部账户都是20个字节，0x开头，string长度40,加上0x，【外部账户-钱包地址，内部账户-合约地址】
		// 以太坊区块hash和交易hash都是0x打头长度33
		// 1.判断是否是块高
		// 2.判断是否是地址
		// 3.不是以上两种情况，就为交易hash或者区块hash，需要都查询
		req.setParameter(req.getParameter().trim());
		String keyword = req.getParameter();
		boolean isNumber = keyword.matches("[0-9]+");
		QueryNavigationResp result = new QueryNavigationResp();
		QueryNavigationStructResp queryNavigationStructResp = new QueryNavigationStructResp();
		if (isNumber) {
			result.setType("block");
			queryNavigationStructResp.setNumber(Long.valueOf(keyword));
		} else {
			// 为false则可能为区块交易hash或者为账户
			if (keyword.length() <= 2)
				throw new BusinessException(i18n.i(I18nEnum.SEARCH_KEYWORD_TOO_SHORT));
			if (keyword.startsWith("0x")) {
				if (keyword.length() == 42) {
					// 合约或账户地址
					result.setType("address");
					queryNavigationStructResp.setAddress(keyword);
				}
				if (keyword.length() == 130) {
					// 节点Id或名称
					result.setType("staking");
					queryNavigationStructResp.setNodeId(keyword);
				}
				if (keyword.length() == 66) {
					/**
					 * 交易hash或者区块hash 逻辑分析 1、优先查询已完成交易 2、已完成交易查询无记录，则查询区块 3、区块查询无记录，则查询待处理交易
					 * 4、以上都无记录，则返回空结果
					 */
					TransactionDetailReq transactionDetailReq = new TransactionDetailReq();
					transactionDetailReq.setTxHash(keyword);
					try {
						// 此处调用如果查询不到交易记录会抛出BusinessException异常
//						TransactionDetail transactionDetail = transactionService.getDetail(transactionDetailReq);
//						result.setType("transaction");
//						queryNavigationStructResp.setTxHash(transactionDetail.getTxHash());
					} catch (BusinessException be) {
						log.info("在交易表查询不到Hash为[{}]的交易记录，尝试查询Hash为[{}]的区块信息...", keyword, keyword);
						BlockExample blockExample = new BlockExample();
						blockExample.createCriteria().andHashEqualTo(keyword);
						List<Block> blockList = blockMapper.selectByExample(blockExample);
						if (blockList.size() > 0) {
							// 如果找到区块信息，则构造结果并返回
							result.setType("block");
							queryNavigationStructResp.setTxHash(keyword);
						}
					}
				}
			} else {
				// 非0x开头，则默认查询节点信息
				result.setType("staking");
				queryNavigationStructResp.setNodeId(keyword);
			}
		}
		result.setStruct(queryNavigationStructResp);
		return result;
	}

	@Override
	public BlockStatisticNewResp blockStatisticNew() {
		/************** 组装图表数据 ************/
		List<BlockRedis> items = statisticCacheService.getBlockCache(1,50);
		BlockStatisticNewResp blockStatisticNewResp = new BlockStatisticNewResp();
		Long[] x = new Long[items.size()];
		Double[] ya = new Double[items.size()];
		Long[] yb = new Long[items.size()];
		for (int i=0;i<items.size();i++){
			BlockRedis currentBlock = items.get(i);
			if(i==0||i==items.size()-1) continue;
			BlockRedis previousBlock = items.get(i-1);
			x[i] = currentBlock.getNumber();
			BigDecimal sec = BigDecimal.valueOf(currentBlock.getTimestamp().getTime()-previousBlock.getTimestamp().getTime()).divide(BigDecimal.valueOf(1000));
			ya[i] = sec.doubleValue();
			if(currentBlock.getStatTxQty()==null) {
				yb[i] = 0l;
			} else {
				yb[i] = Long.valueOf(currentBlock.getStatTxQty());
			}
		}
		blockStatisticNewResp.setX(x);
		blockStatisticNewResp.setYa(ya);
		blockStatisticNewResp.setYb(yb);
		return blockStatisticNewResp;
	}

	@Override
	public ChainStatisticNewResp chainStatisticNew() {
		NetworkStatRedis networkStatRedis = statisticCacheService.getNetworkStatCache();
		ChainStatisticNewResp chainStatisticNewResp = new ChainStatisticNewResp();
		if(networkStatRedis != null) {
			chainStatisticNewResp.setMaxTps(networkStatRedis.getMaxTps());
			chainStatisticNewResp.setAddressQty(networkStatRedis.getAddressQty());
			chainStatisticNewResp.setCurrentNumber(networkStatRedis.getCurrentNumber());
			chainStatisticNewResp.setDoingProposalQty(networkStatRedis.getDoingProposalQty());
			chainStatisticNewResp.setIssueValue(networkStatRedis.getIssueValue());
			chainStatisticNewResp.setNodeId(networkStatRedis.getNodeId());
			chainStatisticNewResp.setNodeName(networkStatRedis.getNodeName());
			chainStatisticNewResp.setProposalQty(networkStatRedis.getProposalQty());
			BigDecimal sum = new BigDecimal(networkStatRedis.getStakingDelegationValue()).add(new BigDecimal(networkStatRedis.getStakingValue()));
			chainStatisticNewResp.setTakingDelegationValue(sum.toString());
			chainStatisticNewResp.setTurnValue(networkStatRedis.getTurnValue());
			chainStatisticNewResp.setTxQty(networkStatRedis.getTxQty());
		}
		return chainStatisticNewResp;
	}

	@Override
	public List<StakingListNewResp> stakingListNew() {
		StakingExample stakingExample = new StakingExample();
		Criteria criteria = stakingExample.createCriteria();
		criteria.andStatusEqualTo(StakingStatus.CANDIDATE.getCode()).andIsConsensusEqualTo(IsConsensusStatus.YES.getCode());
		stakingExample.setOrderByClause("cast(staking_has as UNSIGNED INTEGER) + cast(staking_locked as UNSIGNED INTEGER)"
				+ " + cast(stat_delegate_has as UNSIGNED INTEGER) + cast(stat_delegate_locked as UNSIGNED INTEGER),program_version,id desc");
		List<Staking> stakings = stakingMapper.selectByExample(stakingExample);
		StakingListNewResp stakingListNewResp = new StakingListNewResp();
		List<StakingListNewResp> lists = new LinkedList<>();
		for (int i = 0;i<stakings.size();i++) {
			stakingListNewResp.setExpectedIncome(stakings.get(i).getExpectedIncome());
			stakingListNewResp.setIsInit(stakings.get(i).getIsInit() == 1?true:false);
			stakingListNewResp.setNodeId(stakings.get(i).getNodeId());
			stakingListNewResp.setNodeName(stakings.get(i).getStakingName());
			stakingListNewResp.setRanking(i);
			stakingListNewResp.setStakingIcon(stakings.get(i).getStakingIcon());
			lists.add(stakingListNewResp);
		}
		return lists;
	}

	@Override
	public List<BlockListNewResp> blockListNew() {
		List<BlockRedis> items = statisticCacheService.getBlockCache(1,8);
		BlockListNewResp blockListNewResp = new BlockListNewResp();
		List<BlockListNewResp> lists = new LinkedList<>();
		for (BlockRedis blockRedis : items) {
			blockListNewResp.setNodeId(blockRedis.getNodeId());
			blockListNewResp.setNodeName(blockRedis.getNodeName());
			blockListNewResp.setNumber(blockRedis.getNumber());
			blockListNewResp.setServerTime(new Date().getTime());
			blockListNewResp.setStatTxQty(blockRedis.getStatTxQty());
			blockListNewResp.setTimestamp(blockRedis.getTimestamp().getTime());
			lists.add(blockListNewResp);
		}
		return lists;
	}

}
