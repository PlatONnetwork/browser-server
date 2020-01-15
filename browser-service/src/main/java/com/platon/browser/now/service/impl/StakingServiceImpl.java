package com.platon.browser.now.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.client.PlatOnClient;
import com.platon.browser.common.BrowserConst;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.DelegationAddress;
import com.platon.browser.dto.CustomStaking.StatusEnum;
import com.platon.browser.dto.elasticsearch.ESResult;
import com.platon.browser.dto.DelegationStaking;
import com.platon.browser.elasticsearch.NodeOptESRepository;
import com.platon.browser.elasticsearch.dto.NodeOpt;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilderConstructor;
import com.platon.browser.elasticsearch.service.impl.ESQueryBuilders;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.enums.StakingStatusEnum;
import com.platon.browser.now.service.StakingService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.req.staking.*;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.staking.*;
import com.platon.browser.util.I18nUtil;
import com.platon.sdk.contracts.ppos.dto.resp.Reward;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *  验证人模块方法
 *  @file StakingServiceImpl.java
 *  @description
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Service
public class StakingServiceImpl implements StakingService {

	private final Logger logger = LoggerFactory.getLogger(StakingServiceImpl.class);
	
	@Autowired
	private StatisticCacheService statisticCacheService;

	@Autowired
	private CustomStakingMapper customStakingMapper;

	@Autowired
	private CustomDelegationMapper customDelegationMapper;
	
	@Autowired
	private NodeMapper nodeMapper;

	@Autowired
	private NodeOptESRepository nodeOptESRepository;

	@Autowired
	private I18nUtil i18n;

	@Autowired
    private BlockChainConfig blockChainConfig;

	@Autowired
    private PlatOnClient platonClient;
	
	@Override
	public StakingStatisticNewResp stakingStatisticNew() {
		/** 获取统计信息 */
		NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
		StakingStatisticNewResp stakingStatisticNewResp = new StakingStatisticNewResp();
		if(networkStatRedis != null) {
			BeanUtils.copyProperties(networkStatRedis, stakingStatisticNewResp);
			stakingStatisticNewResp.setCurrentNumber(networkStatRedis.getCurNumber());
			stakingStatisticNewResp.setNextSetting(networkStatRedis.getNextSettle());
			//实时除以现有的结算周期人数
			Integer count= customStakingMapper.selectCountByActive();
			stakingStatisticNewResp.setStakingReward(networkStatRedis.getSettleStakingReward().divide(new BigDecimal(count), 18, RoundingMode.FLOOR));
			
		}
		return stakingStatisticNewResp;
	}

	@Override
	public RespPage<AliveStakingListResp> aliveStakingList(AliveStakingListReq req) {
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		Integer status = null;
		Integer isSettle = null;
		/**
		 *  对前端传的参数进行转换查询条件
		 */
		switch (StakingStatusEnum.valueOf(req.getQueryStatus().toUpperCase())) {
			case ALL:
				/** 查询候选人 */
				status = StakingStatusEnum.CANDIDATE.getCode();
				break;
			case ACTIVE:
				/** 活跃中代表即使后续同时也是结算周期验证人 */
				status = StakingStatusEnum.CANDIDATE.getCode();
				isSettle = CustomStaking.YesNoEnum.YES.getCode();
				break;
			case CANDIDATE:
				/** 查询候选人 */
				status = StakingStatusEnum.CANDIDATE.getCode();
				isSettle = CustomStaking.YesNoEnum.NO.getCode();
				break;
			default:
				break;
		}

		RespPage<AliveStakingListResp> respPage = new RespPage<>();
		List<AliveStakingListResp> lists = new LinkedList<>();
		/** 根据条件和状态进行查询列表 */
		NodeExample nodeExample = new NodeExample();
		nodeExample.setOrderByClause(" big_version desc, total_value desc,staking_block_num asc, staking_tx_index asc");
		NodeExample.Criteria criteria1 = nodeExample.createCriteria();
		criteria1.andStatusEqualTo(status);
		if(StringUtils.isNotBlank(req.getKey())){
			criteria1.andNodeNameLike("%" + req.getKey() + "%");
		}
		if(isSettle != null) {
			criteria1.andIsSettleEqualTo(isSettle);
		}
		nodeExample.or(criteria1);
		/**
		 * 如果节点状态为退出中且为结算周期则认为在活跃中
		 */
		NodeExample.Criteria criteria2 = nodeExample.createCriteria();
		criteria2.andStatusEqualTo(CustomStaking.StatusEnum.EXITING.getCode());
		if(StringUtils.isNotBlank(req.getKey())){
			criteria2.andNodeNameLike("%" + req.getKey() + "%");
		}
		criteria2.andIsSettleEqualTo(CustomStaking.YesNoEnum.YES.getCode());
		if(isSettle != null) {
			criteria1.andIsSettleEqualTo(isSettle);
		}
		nodeExample.or(criteria2);
		
		Page<Node> stakingPage = nodeMapper.selectByExample(nodeExample);
		List<Node> stakings = stakingPage.getResult();
		/** 查询出块节点 */
		NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
		for (int i = 0; i < stakings.size(); i++) {
			AliveStakingListResp aliveStakingListResp = new AliveStakingListResp();
			BeanUtils.copyProperties(stakings.get(i), aliveStakingListResp);
			aliveStakingListResp.setBlockQty(stakings.get(i).getStatBlockQty());
			aliveStakingListResp.setDelegateQty(stakings.get(i).getStatValidAddrs());
			aliveStakingListResp.setExpectedIncome(stakings.get(i).getAnnualizedRate().toString());
			/** 委托总金额数=委托交易总金额(犹豫期金额)+委托交易总金额(锁定期金额) */
			String sumAmount = stakings.get(i).getStatDelegateValue().toString();
			aliveStakingListResp.setDelegateValue(sumAmount);
			aliveStakingListResp.setIsInit(stakings.get(i).getIsInit() == 1);
			aliveStakingListResp.setStakingIcon(stakings.get(i).getNodeIcon());
			if(stakings.get(i).getIsRecommend() != null) {
				aliveStakingListResp.setIsRecommend(CustomStaking.YesNoEnum.YES.getCode() == stakings.get(i).getIsRecommend());
			}
			/** 设置排行 */
			aliveStakingListResp.setRanking(i + 1);
			aliveStakingListResp.setSlashLowQty(stakings.get(i).getStatSlashLowQty());
			aliveStakingListResp.setSlashMultiQty(stakings.get(i).getStatSlashMultiQty());
			/** 如果是对应的出块节点则置为出块中，否则为活跃中或者退出 */
			if(stakings.get(i).getNodeId().equals(networkStatRedis.getNodeId())) {
				aliveStakingListResp.setStatus(StakingStatusEnum.BLOCK.getCode());
			} else {
				aliveStakingListResp.setStatus(StakingStatusEnum.getCodeByStatus(stakings.get(i).getStatus(), stakings.get(i).getIsConsensus(), stakings.get(i).getIsSettle()));
			}
			/** 质押总数=有效的质押+委托 */
			String totalValue = stakings.get(i).getStakingHes().add(stakings.get(i).getStakingLocked())
					.add(stakings.get(i).getStatDelegateValue()).toString();
			aliveStakingListResp.setTotalValue(totalValue);
			aliveStakingListResp.setDeleAnnualizedRate(stakings.get(i).getDeleAnnualizedRate().toString());
			lists.add(aliveStakingListResp);
		}
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		page.setTotal(stakingPage.getTotal());
		respPage.init(page, lists);
		return respPage;
	}

	@Override
	public RespPage<HistoryStakingListResp> historyStakingList(HistoryStakingListReq req) {
		/** 设置只查询退出中和已退出 */
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		List<Integer> status = new ArrayList<>();
		status.add(CustomStaking.StatusEnum.EXITING.getCode());
		status.add(CustomStaking.StatusEnum.EXITED.getCode());
		RespPage<HistoryStakingListResp> respPage = new RespPage<>();
		List<HistoryStakingListResp> lists = new LinkedList<>();
		/** 根据条件和状态进行查询列表 */
		NodeExample nodeExample = new NodeExample();
		nodeExample.setOrderByClause(" leave_time desc");
		NodeExample.Criteria criteria = nodeExample.createCriteria();
		criteria.andStatusIn(status);
		/**
		 * 防止直接退出的节点出现在历史表中
		 */
		criteria.andIsSettleEqualTo(CustomStaking.YesNoEnum.NO.getCode());
		
		if(StringUtils.isNotBlank(req.getKey())) {
			criteria.andNodeNameLike("%" + req.getKey() + "%");
		}
		Page<Node> stakings = nodeMapper.selectByExample(nodeExample);
		
		for (Node stakingNode:stakings.getResult()) {
			HistoryStakingListResp historyStakingListResp = new HistoryStakingListResp();
			BeanUtils.copyProperties(stakingNode, historyStakingListResp);
			if(stakingNode.getLeaveTime()!=null) {
				historyStakingListResp.setLeaveTime(stakingNode.getLeaveTime().getTime());
			}
			historyStakingListResp.setNodeName(stakingNode.getNodeName());
			historyStakingListResp.setStakingIcon(stakingNode.getNodeIcon());
			historyStakingListResp.setSlashLowQty(stakingNode.getStatSlashLowQty());
			historyStakingListResp.setSlashMultiQty(stakingNode.getStatSlashMultiQty());
			/**
			 * 带提取的委托等于hes+lock
			 */
			historyStakingListResp.setStatDelegateReduction(stakingNode.getStatDelegateReleased());
			historyStakingListResp.setStatus(StakingStatusEnum.getCodeByStatus(stakingNode.getStatus(), stakingNode.getIsConsensus(), stakingNode.getIsSettle()));
			historyStakingListResp.setBlockQty(stakingNode.getStatBlockQty());
			lists.add(historyStakingListResp);
		}
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		page.setTotal(stakings.getTotal());
		respPage.init(page, lists);
		return respPage;
	}

	@Override
	public BaseResp<StakingDetailsResp> stakingDetails(StakingDetailsReq req) {
		/**
		 * 先查询是否活跃节点，查不到再查询是否历史汇总
		 */
		Node stakingNode = nodeMapper.selectByPrimaryKey(req.getNodeId());
		StakingDetailsResp resp = new StakingDetailsResp();
		// 只有一条数据
		if (stakingNode != null) {
			BeanUtils.copyProperties(stakingNode, resp);
			resp.setIsInit(stakingNode.getIsInit() == 1);
			resp.setStatus(StakingStatusEnum.getCodeByStatus(stakingNode.getStatus(), stakingNode.getIsConsensus(), stakingNode.getIsSettle()));
			resp.setSlashLowQty(stakingNode.getStatSlashLowQty());
			resp.setSlashMultiQty(stakingNode.getStatSlashMultiQty());
			resp.setBlockQty(stakingNode.getStatBlockQty());
			resp.setExpectBlockQty(stakingNode.getStatExpectBlockQty());
			resp.setVerifierTime(stakingNode.getStatVerifierTime());
			resp.setJoinTime(stakingNode.getJoinTime().getTime());
			resp.setDenefitAddr(stakingNode.getBenefitAddr());
			resp.setStakingIcon(stakingNode.getNodeIcon());
			resp.setDeleAnnualizedRate(stakingNode.getDeleAnnualizedRate().toString());
			resp.setRewardPer(new BigDecimal(stakingNode.getRewardPer()).divide(BrowserConst.PERCENTAGE).toString());
			/**
			 * 待领取奖励等于 累积委托奖励减去已领取委托奖励
			 */
			resp.setDeleRewardRed(stakingNode.getTotalDeleReward().subtract(stakingNode.getHaveDeleReward()));
			/** 只有不是内置节点才计算年化率  */
			if (CustomStaking.YesNoEnum.YES.getCode() != stakingNode.getIsInit()) {
				resp.setExpectedIncome(String.valueOf(stakingNode.getAnnualizedRate()));
				resp.setRewardValue(stakingNode.getStatFeeRewardValue().add(stakingNode.getStatBlockRewardValue())
						.add(stakingNode.getStatStakingRewardValue()));
			} else {
				resp.setRewardValue(stakingNode.getStatFeeRewardValue());
				resp.setExpectedIncome("");
			}
			String webSite = "";
			if (StringUtils.isNotBlank(stakingNode.getWebSite())) {
				/**
				 * 如果地址不是http开头就补齐
				 */
				if (stakingNode.getWebSite().startsWith(BrowserConst.HTTP) || stakingNode.getWebSite().startsWith(BrowserConst.HTTPS)) {
					webSite = stakingNode.getWebSite();
				} else {
					webSite = BrowserConst.HTTP + stakingNode.getWebSite();
				}
			}
			resp.setWebsite(webSite);
			/** 实际跳转地址是url拼接上名称 */
			if (StringUtils.isNotBlank(stakingNode.getExternalName())) {
				resp.setExternalUrl(blockChainConfig.getKeyBase() + stakingNode.getExternalName());
			} else {
				resp.setExternalUrl(blockChainConfig.getKeyBase());
			}
			if (stakingNode.getLeaveTime() != null) {
				resp.setLeaveTime(stakingNode.getLeaveTime().getTime());
			}
			/**
			 * 如果判断为true则表示为查历史数据
			 * 没有值则标识查询活跃账户
			 */
			if(stakingNode.getStatus().intValue() == StatusEnum.CANDIDATE.getCode()) {
				resp.setDelegateQty(stakingNode.getStatValidAddrs());
				/** 候选中则返回单条委托的总金额 **/
				resp.setDelegateValue(stakingNode.getStatDelegateValue());
				/** 质押金额=质押（犹豫期）+ 质押（锁定期）  */
				BigDecimal stakingValue = stakingNode.getStakingHes().add(stakingNode.getStakingLocked());
				resp.setStakingValue(stakingValue);
				/** 质押总数=有效的质押+委托 */
				BigDecimal totalValue = stakingValue.add(stakingNode.getStatDelegateValue());
				resp.setTotalValue(totalValue);
			} else {
				resp.setDelegateQty(stakingNode.getStatInvalidAddrs());
				resp.setDelegateValue(stakingNode.getStatDelegateValue());
				resp.setStakingValue(stakingNode.getStakingReduction());
				BigDecimal totalValue = resp.getStakingValue().add(resp.getDelegateValue());
				resp.setTotalValue(totalValue);
				resp.setStatDelegateReduction(resp.getDelegateValue().add(stakingNode.getStatDelegateReleased()));
			}
		}
		return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), resp);
	}

	@Override
	public RespPage<StakingOptRecordListResp> stakingOptRecordList( StakingOptRecordListReq req) {
		
		ESQueryBuilderConstructor constructor = new ESQueryBuilderConstructor();
		constructor.must(new ESQueryBuilders().term("nodeId", req.getNodeId()));
		ESResult<NodeOpt> items = new ESResult<>();
		constructor.setDesc("id");
		try {
			items = nodeOptESRepository.search(constructor, NodeOpt.class, req.getPageNo(),req.getPageSize());
		} catch (IOException e) {
			logger.error("获取区块错误。", e);
		}
		List<NodeOpt> nodeOpts = items.getRsData();
		RespPage<StakingOptRecordListResp> respPage = new RespPage<>();
		List<StakingOptRecordListResp> lists = new LinkedList<>();
		for (NodeOpt nodeOpt: nodeOpts) {
			StakingOptRecordListResp stakingOptRecordListResp = new StakingOptRecordListResp();
			BeanUtils.copyProperties(nodeOpt, stakingOptRecordListResp);
			stakingOptRecordListResp.setType(String.valueOf(nodeOpt.getType()));
			stakingOptRecordListResp.setTimestamp(nodeOpt.getTime().getTime());
			stakingOptRecordListResp.setBlockNumber(nodeOpt.getBNum());
			if(StringUtils.isNotBlank(nodeOpt.getDesc())) {
				String[] desces = nodeOpt.getDesc().split(BrowserConst.OPT_SPILT);
				/** 根据不同类型组合返回 */
				switch (NodeOpt.TypeEnum.getEnum(String.valueOf(nodeOpt.getType()))) {
					/**
					 *修改验证人
					 */
					case MODIFY:
						if(desces.length > 1) {
							stakingOptRecordListResp.setBeforeRate(new BigDecimal(desces[0]).divide(BrowserConst.PERCENTAGE).toString());
							stakingOptRecordListResp.setAfterRate(new BigDecimal(desces[1]).divide(BrowserConst.PERCENTAGE).toString());
						}
						break;
					/** 提案类型 */
					case PROPOSALS:
						stakingOptRecordListResp.setId(BrowserConst.PIP_NAME + desces[0]);
						stakingOptRecordListResp.setTitle(BrowserConst.INQUIRY.equals(desces[1])?"":desces[1]);
						stakingOptRecordListResp.setProposalType(desces[2]);
						if(desces.length > 3) {
							stakingOptRecordListResp.setVersion(desces[3]);
						}
						break;
					/** 投票类型 */
					case VOTE:
						stakingOptRecordListResp.setId(BrowserConst.PIP_NAME + desces[0]);
						stakingOptRecordListResp.setTitle(BrowserConst.INQUIRY.equals(desces[1])?"":desces[1]);
						stakingOptRecordListResp.setOption(desces[2]);
						stakingOptRecordListResp.setProposalType(desces[3]);
						if(desces.length > 4) {
							stakingOptRecordListResp.setVersion(desces[4]);
						}
						break;
					/** 双签 */
					case MULTI_SIGN:
						stakingOptRecordListResp.setPercent(desces[0]);
						stakingOptRecordListResp.setAmount(new BigDecimal(desces[1]));
						break;
					/** 出块率低 */
					case LOW_BLOCK_RATE:
						stakingOptRecordListResp.setPercent(desces[0]);
						stakingOptRecordListResp.setAmount(new BigDecimal(desces[2]));
						stakingOptRecordListResp.setIsFire(Integer.parseInt(desces[3]));
						break;
						/**
						 * 参数提案
						 */
					case PARAMETER:
						stakingOptRecordListResp.setId(BrowserConst.PIP_NAME + desces[0]);
						stakingOptRecordListResp.setTitle(BrowserConst.INQUIRY.equals(desces[1])?"":desces[1]);
						stakingOptRecordListResp.setProposalType(desces[2]);
						stakingOptRecordListResp.setType("4");
						break;
					default:
						break;
				}
			}

			lists.add(stakingOptRecordListResp);
		}
		/** 查询分页总数 */
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		page.setTotal(items.getTotal());
		respPage.init(page, lists);
		return respPage;
	}

	@Override
	public RespPage<DelegationListByStakingResp> delegationListByStaking( DelegationListByStakingReq req) {
		Node node = nodeMapper.selectByPrimaryKey(req.getNodeId());
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		List<DelegationListByStakingResp> lists = new LinkedList<>();
		/** 根据节点id和区块查询验证委托信息 */
		Page<DelegationStaking> delegationStakings = customDelegationMapper.selectStakingByNodeId(req.getNodeId());
		for (DelegationStaking delegationStaking: delegationStakings.getResult()) {
			DelegationListByStakingResp byStakingResp = new DelegationListByStakingResp();
			BeanUtils.copyProperties(delegationStaking, byStakingResp);
			byStakingResp.setDelegateAddr(delegationStaking.getDelegateAddr());
		   /**已锁定委托（LAT）如果关联的验证人状态正常则正常显示，如果其他情况则为零（delegation）  */
			byStakingResp.setDelegateTotalValue(node.getStatDelegateValue());
			/**
			 * 委托金额等于has加上实际lock金额
			 */
			BigDecimal delValue = delegationStaking.getDelegateHes()
					.add(delegationStaking.getDelegateLocked());
			byStakingResp.setDelegateValue(delValue);
			lists.add(byStakingResp);
		}
		/** 分页统计总数 */
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		page.setTotal(delegationStakings.getTotal());
		RespPage<DelegationListByStakingResp> respPage = new RespPage<>();
		respPage.init(page, lists);
		return respPage;
	}

	@Override
	public RespPage<DelegationListByAddressResp> delegationListByAddress( DelegationListByAddressReq req) {
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		List<DelegationListByAddressResp> lists = new LinkedList<>();
		/** 根据地址分页查询委托列表 */
		Page<DelegationAddress> delegationAddresses =
				customDelegationMapper.selectAddressByAddr(req.getAddress());
		/**
		 * 初始化奖励节点id列表，用来后续查询对应的待领取奖励使用
		 */
		List<String> nodes = new ArrayList<>();
		for (DelegationAddress delegationAddress:  delegationAddresses.getResult()) {
			nodes.add(delegationAddress.getNodeId());
		}
		/**
		 * 获取每一个节点的待领取奖励
		 */
		List<Reward> rewards = new ArrayList<>();
		try {
			rewards = platonClient.getRewardContract().getDelegateReward(req.getAddress(), nodes).send().getData();
		} catch (Exception e) {
			logger.error("{}",e.getMessage());
		}
		for (DelegationAddress delegationAddress:  delegationAddresses.getResult()) {
			DelegationListByAddressResp byAddressResp = new DelegationListByAddressResp();
			BeanUtils.copyProperties(delegationAddress, byAddressResp);
			byAddressResp.setDelegateHas(delegationAddress.getDelegateHes());
			/** 委托金额=犹豫期金额加上锁定期金额 */
			BigDecimal deleValue = delegationAddress.getDelegateHes()
					.add(byAddressResp.getDelegateLocked());
			byAddressResp.setDelegateValue(deleValue);
			byAddressResp.setDelegateUnlock(delegationAddress.getDelegateHes());
			/**
			 * 循环获取奖励
			 */
			for(Reward reward: rewards) {
				/**
				 * 匹配成功之后设置金额
				 */
				if(delegationAddress.getNodeId().equals(reward.getNodeId())) {
					byAddressResp.setDelegateClaim(new BigDecimal(reward.getReward()));
				}
			}
			lists.add(byAddressResp);
		}
		/** 统计总数 */
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		page.setTotal(delegationAddresses.getTotal());
		RespPage<DelegationListByAddressResp> respPage = new RespPage<>();
		respPage.init(page, lists);
		return respPage;
	}

}
