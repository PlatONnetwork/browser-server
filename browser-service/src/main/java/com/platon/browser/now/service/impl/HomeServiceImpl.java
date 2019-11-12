package com.platon.browser.now.service.impl;

import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.elasticsearch.BlockESRepository;
import com.platon.browser.elasticsearch.TransactionESRepository;
import com.platon.browser.elasticsearch.dto.Block;
import com.platon.browser.elasticsearch.dto.ESResult;
import com.platon.browser.elasticsearch.dto.Transaction;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilderConstructor;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilders;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.now.service.CommonService;
import com.platon.browser.now.service.HomeService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.req.home.QueryNavigationRequest;
import com.platon.browser.res.home.*;
import com.platon.browser.util.I18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 *  首页接口逻辑具体实现方法
 *  @file HomeServiceImpl.java
 *  @description
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Slf4j
@Service
public class HomeServiceImpl implements HomeService {

	@Autowired
	private BlockESRepository blockESRepository;
	@Autowired
	private TransactionESRepository transactionESRepository;
	@Autowired
	private StatisticCacheService statisticCacheService;
	@Autowired
	private I18nUtil i18n;
	@Autowired
	private NodeMapper nodeMapper;
	@Autowired
	private BlockChainConfig blockChainConfig;
	@Autowired
	private CommonService commonService;

	/** 记录刷新值 */
	private static Integer consensusNum = 0;

	/** 记录最新块高 */
	private static Long newBlockNum = 0l;

	@Override
	public QueryNavigationResp queryNavigation(QueryNavigationRequest req) {
		/** 以太坊内部和外部账户都是20个字节，0x开头，string长度40,加上0x，【外部账户-钱包地址，内部账户-合约地址】
		* 以太坊区块hash和交易hash都是0x打头长度33
		* 1.判断是否是块高
		* 2.判断是否是地址
		* 3.不是以上两种情况，就为交易hash或者区块hash，需要都查询
		*/
		req.setParameter(req.getParameter().trim());
		String keyword = req.getParameter();
		/** 判断是否为纯数字 */
		boolean isNumber = keyword.matches("[0-9]+");
		QueryNavigationResp result = new QueryNavigationResp();
		QueryNavigationStructResp queryNavigationStructResp = new QueryNavigationStructResp();
		if (isNumber) {
			Long number;
			try {
				/** 转换失败则超出long的数字范围，则认为无效区块号  */
				number = Long.valueOf(keyword);
			} catch (Exception e) {
				throw new BusinessException(i18n.i(I18nEnum.SEARCH_KEYWORD_NO_RESULT));
			}
			/** 存在区块信息则返回区块号 */
			ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
			constructor.must(new ESQueryBuilders().term("num", number));
			ESResult<Block> blockList = new ESResult<>();
			try {
				blockList = blockESRepository.search(constructor, Block.class, 1, 1);
			} catch (IOException e) {
				log.error("获取区块错误。", e);
			}
			if(blockList.getTotal().intValue() > 0) {
				result.setType("block");
				queryNavigationStructResp.setNumber(number);
			}
		} else {
			/** 为false则可能为区块交易hash或者为账户  */
			if (keyword.length() <= 2)
				/** 小于两位的则认为不是正确hash */
				throw new BusinessException(i18n.i(I18nEnum.SEARCH_KEYWORD_TOO_SHORT));
			if (keyword.startsWith("0x")) {
				if (keyword.length() == 42) {
					/** 判断为合约或账户地址 */
					result.setType("address");
					queryNavigationStructResp.setAddress(keyword);
				}
				if (keyword.length() == 130) {
					/** 判断为节点Id */
					Node node = nodeMapper.selectByPrimaryKey(keyword);
					if(node != null) {
						result.setType("staking");
						queryNavigationStructResp.setNodeId(node.getNodeId());
					}
				}
				if (keyword.length() == 66) {
					/**
					 * 交易hash或者区块hash 逻辑分析 1、优先查询已完成交易 2、已完成交易查询无记录，则查询区块
					 * 4、以上都无记录，则返回空结果
					 */
					ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
					constructor.must(new ESQueryBuilders().term("hash", keyword));
					ESResult<Transaction> items = new ESResult<>();
					try {
						items = transactionESRepository.search(constructor, Transaction.class, 1, 1);
					} catch (IOException e) {
						log.error("获取区块错误。", e);
					}
					if(items.getTotal().intValue() > 0) {
						result.setType("transaction");
						queryNavigationStructResp.setTxHash(keyword);
					} else {
						log.debug("在交易表查询不到Hash为[{}]的交易记录，尝试查询Hash为[{}]的区块信息...", keyword, keyword);
						
						ESQueryBuilderConstructor blockConstructor = new ESQueryBuilderConstructor();
						blockConstructor.must(new ESQueryBuilders().term("hash", keyword));
						ESResult<Block> blockList = new ESResult<>();
						try {
							blockList = blockESRepository.search(blockConstructor, Block.class, 1, 1);
						} catch (IOException e) {
							log.error("获取区块错误。", e);
						}
						if (blockList.getTotal() > 0l) {
							/**  如果找到区块信息，则构造结果并返回  */
							result.setType("block");
							queryNavigationStructResp.setNumber(blockList.getRsData().get(0).getNum());
						}
					}
				}
			} else {
				/** 非0x开头，则默认查询节点信息 */
				NodeExample nodeExample = new NodeExample();
				NodeExample.Criteria criteria = nodeExample.createCriteria();
				criteria.andNodeNameEqualTo(keyword);
				List<Node> nodes = nodeMapper.selectByExample(nodeExample);
				if(!nodes.isEmpty()) {
					result.setType("staking");
					queryNavigationStructResp.setNodeId(nodes.get(0).getNodeId());
				}
			}
		}
		result.setStruct(queryNavigationStructResp);
		return result;
	}

	@Override
	public BlockStatisticNewResp blockStatisticNew() {
		/************** 组装图表数据 ************/
		List<Block> items = statisticCacheService.getBlockCache(0,32);
		BlockStatisticNewResp blockStatisticNewResp = new BlockStatisticNewResp();
		if(items.size() == 0) {
			return blockStatisticNewResp;
		}
		/** 查询32条，要进行出块时间扣减，故size减去2 */
		Long[] x = new Long[items.size()- 2];
		Double[] ya = new Double[items.size()- 2];
		Long[] yb = new Long[items.size()- 2];
		for (int i=0;i<items.size() - 1;i++){
			Block currentBlock = items.get(i);
			if(i < items.size() - 2) {
				/** 最后一个扣减不需要对应的设置 */
				x[i] = currentBlock.getNum();
				/** 区块交易数等于null则认为交易为空 */
				if(currentBlock.getTxQty()==null) {
					yb[i] = 0l;
				} else {
					yb[i] = Long.valueOf(currentBlock.getTxQty());
				}
			}
			/** 第一个区块不需要计算出块时间 */
			if(i==0) continue;
			Block previousBlock = items.get(i-1);
			BigDecimal sec = BigDecimal.valueOf(previousBlock.getTime().getTime()-currentBlock.getTime().getTime())
					.divide(BigDecimal.valueOf(1000));
			ya[i-1] = sec.doubleValue();
		}
		blockStatisticNewResp.setX(x);
		blockStatisticNewResp.setYa(ya);
		blockStatisticNewResp.setYb(yb);
		return blockStatisticNewResp;
	}

	@Override
	public ChainStatisticNewResp chainStatisticNew() {
		NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
		ChainStatisticNewResp chainStatisticNewResp = new ChainStatisticNewResp();
		if(networkStatRedis == null) return chainStatisticNewResp;
		/** 查询redis统计信息并转换对应返回对象 */
		BeanUtils.copyProperties(networkStatRedis, chainStatisticNewResp);
		chainStatisticNewResp.setCurrentNumber(networkStatRedis.getCurNumber());
		chainStatisticNewResp.setIssueValue(networkStatRedis.getIssueValue().toString());
		chainStatisticNewResp.setTurnValue(networkStatRedis.getTurnValue().toString());
		chainStatisticNewResp.setStakingDelegationValue(networkStatRedis.getStakingDelegationValue().toString());
		Long bNumber = networkStatRedis.getCurNumber();
		/** 查询缓存最新的八条区块信息 */
		List<Block> items = statisticCacheService.getBlockCache(0,8);
		if(items.size() > 0 ) {
			/**
			 * 如果统计区块小于区块交易则重新查询新的区块
			 */
			Long dValue = items.get(0).getNum()-bNumber;
			if(dValue > 0) {
				items = statisticCacheService.getBlockCacheByStartEnd(dValue, dValue+8);
			}
		}

		List<BlockListNewResp> lists = new LinkedList<>();
		for (int i=0; i < items.size(); i++) {
			BlockListNewResp blockListNewResp = new BlockListNewResp();
			BeanUtils.copyProperties(items.get(i), blockListNewResp);
			blockListNewResp.setNodeId(items.get(i).getNodeId());
			blockListNewResp.setNumber(items.get(i).getNum());
			blockListNewResp.setStatTxQty(items.get(i).getTxQty());
			blockListNewResp.setServerTime(new Date().getTime());
			blockListNewResp.setTimestamp(items.get(i).getTime().getTime());
			blockListNewResp.setIsRefresh(true);
			blockListNewResp.setNodeName(commonService.getNodeName(items.get(i).getNodeId()));
			/**
			 * 第一个块需要记录缓存，然后进行比对
			 * 如果块没有增长则置为false
			 */
			if(i == 0) {
				log.debug("newBlockNum:{},item number:{},isFresh:{}",newBlockNum , items.get(i).getNum(),blockListNewResp.getIsRefresh());
				if(items.get(i).getNum().longValue() != newBlockNum.longValue()) {
					newBlockNum = items.get(i).getNum();
				} else {
					blockListNewResp.setIsRefresh(false);
				}
			}
			lists.add(blockListNewResp);
		}
		chainStatisticNewResp.setBlockList(lists);
		return chainStatisticNewResp;
	}

	@Override
	public StakingListNewResp stakingListNew() {

		StakingListNewResp stakingListNewResp = new StakingListNewResp();
		stakingListNewResp.setIsRefresh(false);
		NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
		/** 当前区块除以共识区块算出第几轮 */
		BigDecimal num = new BigDecimal(networkStatRedis.getCurNumber()).divide(new BigDecimal(blockChainConfig.getConsensusPeriodBlockCount()))
				.setScale(0, RoundingMode.UP);
		if(num.intValue() > consensusNum) {
			/** 现有共识轮数大于存储轮则全量刷新  */
			stakingListNewResp.setIsRefresh(true);
			consensusNum = num.intValue();
		}
		/** 只查询活跃的节点，并倒序返回   */
		NodeExample nodeExample = new NodeExample();
		NodeExample.Criteria criteria = nodeExample.createCriteria();
		criteria.andStatusEqualTo(CustomStaking.StatusEnum.CANDIDATE.getCode()).andIsConsensusEqualTo(CustomStaking.YesNoEnum.YES.getCode());
		nodeExample.setOrderByClause(" big_version desc,total_value desc,staking_block_num desc ,staking_tx_index desc");
		List<Node> nodes = nodeMapper.selectByExample(nodeExample);

		List<StakingListResp> lists = new LinkedList<>();
		for (int i = 0;i<nodes.size();i++) {
			StakingListResp stakingListResp = new StakingListResp();
			BeanUtils.copyProperties(nodes.get(i), stakingListResp);
			/** 只有不是内置节点才计算年化率  */
			if(CustomStaking.YesNoEnum.YES.getCode() != nodes.get(i).getIsInit()) {
				stakingListResp.setExpectedIncome(nodes.get(i).getAnnualizedRate().toString() + "%");
			} else {
				stakingListResp.setExpectedIncome("");
			}
			stakingListResp.setIsInit(nodes.get(i).getIsInit() == 1);
			stakingListResp.setNodeName(nodes.get(i).getNodeName());
			/** 质押总数=有效的质押+委托 */
			String totalValue = nodes.get(i).getStakingHes().add(nodes.get(i).getStakingLocked())
					.add(nodes.get(i).getStatDelegateValue()).toString();
			stakingListResp.setTotalValue(totalValue);
			stakingListResp.setRanking(i+1);
			lists.add(stakingListResp);
		}
		stakingListNewResp.setDataList(lists);
		return stakingListNewResp;
	}

	@Override
	public List<BlockListNewResp> blockListNew() {
//		NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
//		Long bNumber = networkStatRedis.getCurrentNumber();
//		/** 查询缓存最新的八条区块信息 */
//		List<Block> items = statisticCacheService.getBlockCache(0,8);
//		if(items.size() > 0 ) {
//			/**
//			 * 如果统计区块小于区块交易则重新查询新的区块
//			 */
//			Long dValue = items.get(0).getNumber()-bNumber;
//			if(dValue > 0) {
//				items = statisticCacheService.getBlockCache(dValue.intValue()/8+1, 8);
//			}
//		}
//
		List<BlockListNewResp> lists = new LinkedList<>();
//		for (int i=0; i < items.size(); i++) {
//			BlockListNewResp blockListNewResp = new BlockListNewResp();
//			BeanUtils.copyProperties(items.get(i), blockListNewResp);
//			blockListNewResp.setServerTime(new Date().getTime());
//			blockListNewResp.setTimestamp(items.get(i).getTimestamp().getTime());
//			blockListNewResp.setIsRefresh(true);
//			/**
//			 * 第一个块需要记录缓存，然后进行比对
//			 * 如果块没有增长则置为false
//			 */
//			if(i == 0) {
//				log.debug("newBlockNum:{},item number:{},isFresh:{}",newBlockNum , items.get(i).getNumber(),blockListNewResp.getIsRefresh());
//				if(items.get(i).getNumber().longValue() != newBlockNum.longValue()) {
//					newBlockNum = items.get(i).getNumber();
//				} else {
//					blockListNewResp.setIsRefresh(false);
//				}
//			}
//			lists.add(blockListNewResp);
//		}
		return lists;
	}

}
