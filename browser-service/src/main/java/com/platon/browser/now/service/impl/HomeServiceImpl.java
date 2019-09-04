package com.platon.browser.now.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.CustomStaking;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingExample;
import com.platon.browser.dao.entity.StakingExample.Criteria;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.now.service.HomeService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.req.home.QueryNavigationRequest;
import com.platon.browser.res.home.BlockListNewResp;
import com.platon.browser.res.home.BlockStatisticNewResp;
import com.platon.browser.res.home.ChainStatisticNewResp;
import com.platon.browser.res.home.QueryNavigationResp;
import com.platon.browser.res.home.QueryNavigationStructResp;
import com.platon.browser.res.home.StakingListNewResp;
import com.platon.browser.res.home.StakingListResp;
import com.platon.browser.util.I18nUtil;

import lombok.extern.slf4j.Slf4j;

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
	private BlockMapper blockMapper;
	@Autowired
	private TransactionMapper transactionMapper;
	@Autowired
	private StatisticCacheService statisticCacheService;
	@Autowired
	private I18nUtil i18n;
	@Autowired
	private StakingMapper stakingMapper;
	@Autowired
	private BlockChainConfig blockChainConfig;
	
	/** 记录刷新值 */
	private static Integer consensusNum = 0;

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
			Block block = blockMapper.selectByPrimaryKey(number);
			if(block != null) {
				result.setType("block");
				queryNavigationStructResp.setNumber(Long.valueOf(keyword));
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
					StakingExample stakingExample = new StakingExample();
					StakingExample.Criteria criteria = stakingExample.createCriteria();
					criteria.andNodeIdEqualTo(keyword);
					List<Staking> stakings = stakingMapper.selectByExample(stakingExample);
					if(stakings.size() > 0) {
						result.setType("staking");
						queryNavigationStructResp.setNodeId(stakings.get(0).getNodeId());
					}
				}
				if (keyword.length() == 66) {
					/**
					 * 交易hash或者区块hash 逻辑分析 1、优先查询已完成交易 2、已完成交易查询无记录，则查询区块 
					 * 4、以上都无记录，则返回空结果
					 */
					Transaction transaction = transactionMapper.selectByPrimaryKey(keyword);
					if(transaction != null) {
						result.setType("transaction");
						queryNavigationStructResp.setTxHash(transaction.getHash());
					} else {
						log.info("在交易表查询不到Hash为[{}]的交易记录，尝试查询Hash为[{}]的区块信息...", keyword, keyword);
						BlockExample blockExample = new BlockExample();
						blockExample.createCriteria().andHashEqualTo(keyword);
						List<Block> blockList = blockMapper.selectByExample(blockExample);
						if (blockList.size() > 0) {
							/**  如果找到区块信息，则构造结果并返回  */
							result.setType("block");
							queryNavigationStructResp.setTxHash(keyword);
						}
					}
				}
			} else {
				/** 非0x开头，则默认查询节点信息 */
				StakingExample stakingExample = new StakingExample();
				StakingExample.Criteria criteria = stakingExample.createCriteria();
				criteria.andStakingNameEqualTo(keyword);
				List<Staking> stakings = stakingMapper.selectByExample(stakingExample);
				if(stakings.size() > 0) {
					result.setType("staking");
					queryNavigationStructResp.setNodeId(stakings.get(0).getNodeId());
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
		/** 查询32条，要进行出块时间扣减，故size减去2 */
		Long[] x = new Long[items.size()- 2];
		Double[] ya = new Double[items.size()- 2];
		Long[] yb = new Long[items.size()- 2];
		for (int i=0;i<items.size() - 2;i++){
			Block currentBlock = items.get(i);
			x[i] = currentBlock.getNumber();
			/** 区块交易数等于null则认为交易为空 */
			if(currentBlock.getStatTxQty()==null) {
				yb[i] = 0l;
			} else {
				yb[i] = Long.valueOf(currentBlock.getStatTxQty());
			}
			/** 第一个区块和最后一个区块才计算出块时间 */
			if(i==0||i==items.size()-1) continue;
			Block previousBlock = items.get(i-1);
			BigDecimal sec = BigDecimal.valueOf(previousBlock.getTimestamp().getTime()-currentBlock.getTimestamp().getTime())
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
		if(networkStatRedis != null) {
			/** 查询redis统计信息并转换对应返回对象 */
			BeanUtils.copyProperties(networkStatRedis, chainStatisticNewResp);
			BigDecimal sum = new BigDecimal(networkStatRedis.getStakingDelegationValue()).add(new BigDecimal(networkStatRedis.getStakingValue()));
			chainStatisticNewResp.setStakingDelegationValue(sum.toString());
		}
		return chainStatisticNewResp;
	}

	@Override
	public StakingListNewResp stakingListNew() {
		
		StakingListNewResp stakingListNewResp = new StakingListNewResp();
		stakingListNewResp.setIsRefresh(false);
		NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
		/** 当前区块除以共识区块算出第几轮 */
		BigDecimal num = new BigDecimal(networkStatRedis.getCurrentNumber()).divide(new BigDecimal(blockChainConfig.getConsensusPeriodBlockCount()))
				.setScale(0, RoundingMode.DOWN);
		if(num.intValue() > consensusNum) {
			/** 现有共识轮数大于存储轮则全量刷新  */
			stakingListNewResp.setIsRefresh(true);
			consensusNum = num.intValue(); 
		}
		/** 只查询活跃的节点，并倒序返回   */
		StakingExample stakingExample = new StakingExample();
		Criteria criteria = stakingExample.createCriteria();
		criteria.andStatusEqualTo(CustomStaking.StatusEnum.CANDIDATE.getCode()).andIsConsensusEqualTo(CustomStaking.YesNoEnum.YES.getCode());
		stakingExample.setOrderByClause("cast(staking_has as Decimal(30)) + cast(staking_locked  as Decimal(30))"
				+ " + cast(stat_delegate_has  as Decimal(30)) + cast(stat_delegate_locked  as Decimal(30)) desc,program_version desc,staking_addr desc,node_id desc,staking_block_num desc");
		List<Staking> stakings = stakingMapper.selectByExample(stakingExample);
		 
		List<StakingListResp> lists = new LinkedList<>();
		for (int i = 0;i<stakings.size();i++) {
			StakingListResp stakingListResp = new StakingListResp();
			BeanUtils.copyProperties(stakings.get(i), stakingListResp);
			stakingListResp.setExpectedIncome(stakings.get(i).getExpectedIncome() + "%");
			stakingListResp.setIsInit(stakings.get(i).getIsInit() == 1?true:false);
			stakingListResp.setNodeName(stakings.get(i).getStakingName());
			/** 质押总数=有效的质押+委托 */
			String totalValue = new BigDecimal(stakings.get(i).getStakingHas()).add(new BigDecimal(stakings.get(i).getStakingLocked()))
					.add(new BigDecimal(stakings.get(i).getStatDelegateHas())).add(new BigDecimal(stakings.get(i).getStatDelegateLocked())).toString();
			stakingListResp.setTotalValue(totalValue);
			stakingListResp.setRanking(i+1);
			lists.add(stakingListResp);
		}
		stakingListNewResp.setDataList(lists);
		return stakingListNewResp;
	}

	@Override
	public List<BlockListNewResp> blockListNew() {
		/** 查询缓存最新的八条区块信息 */
		List<Block> items = statisticCacheService.getBlockCache(0,8);
		List<BlockListNewResp> lists = new LinkedList<>();
		for (Block blockRedis : items) {
			BlockListNewResp blockListNewResp = new BlockListNewResp();
			BeanUtils.copyProperties(blockRedis, blockListNewResp);
			blockListNewResp.setServerTime(new Date().getTime());
			blockListNewResp.setTimestamp(blockRedis.getTimestamp().getTime());
			lists.add(blockListNewResp);
		}
		return lists;
	}

}
