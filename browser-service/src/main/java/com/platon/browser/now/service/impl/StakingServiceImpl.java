package com.platon.browser.now.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.platon.browser.dao.mapper.NodeOptMapper;
import com.platon.browser.dao.mapper.StakingMapper;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.common.BrowserConst;
import com.platon.browser.config.BlockChainConfig;
import com.platon.browser.dao.entity.Address;
import com.platon.browser.dao.entity.DelegationStaking;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.NodeOpt;
import com.platon.browser.dao.entity.NodeOptExample;
import com.platon.browser.dao.entity.Staking;
import com.platon.browser.dao.entity.StakingKey;
import com.platon.browser.dao.entity.StakingNode;
import com.platon.browser.dao.mapper.AddressMapper;
import com.platon.browser.dao.mapper.CustomDelegationMapper;
import com.platon.browser.dao.mapper.CustomStakingMapper;
import com.platon.browser.dto.CustomNodeOpt;
import com.platon.browser.dto.CustomStaking;
import com.platon.browser.dto.CustomStaking.StatusEnum;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.enums.StakingStatusEnum;
import com.platon.browser.now.service.StakingService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.req.staking.AliveStakingListReq;
import com.platon.browser.req.staking.DelegationListByAddressReq;
import com.platon.browser.req.staking.DelegationListByStakingReq;
import com.platon.browser.req.staking.HistoryStakingListReq;
import com.platon.browser.req.staking.StakingDetailsReq;
import com.platon.browser.req.staking.StakingOptRecordListReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.RespPage;
import com.platon.browser.resp.staking.AliveStakingListResp;
import com.platon.browser.resp.staking.DelegationListByAddressResp;
import com.platon.browser.resp.staking.DelegationListByStakingResp;
import com.platon.browser.resp.staking.HistoryStakingListResp;
import com.platon.browser.resp.staking.StakingChangeNewResp;
import com.platon.browser.resp.staking.StakingDetailsResp;
import com.platon.browser.resp.staking.StakingOptRecordListResp;
import com.platon.browser.resp.staking.StakingStatisticNewResp;
import com.platon.browser.util.I18nUtil;

/**
 *  验证人模块方法
 *  @file StakingServiceImpl.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Service
public class StakingServiceImpl implements StakingService {

	@Autowired
	private StatisticCacheService statisticCacheService;

	@Autowired
	private CustomStakingMapper customStakingMapper;
	
	@Autowired
	private StakingMapper stakingMapper;
	
	@Autowired
	private CustomDelegationMapper customDelegationMapper;
	
	@Autowired
	private NodeOptMapper nodeOptMapper;
	
	@Autowired
	private AddressMapper addressMapper;

	@Autowired
	private I18nUtil i18n;
	
	@Autowired
    private BlockChainConfig blockChainConfig;
	
	@Override
	public StakingStatisticNewResp stakingStatisticNew() {
		/** 获取统计信息 */
		NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
		StakingStatisticNewResp stakingStatisticNewResp = new StakingStatisticNewResp();
		if(networkStatRedis != null) {
			BeanUtils.copyProperties(networkStatRedis, stakingStatisticNewResp);
			
			String sumExitDelegate = customStakingMapper.selectSumExitDelegate();
			String stakingDelegationValue = new BigDecimal(networkStatRedis.getStakingDelegationValue())
					.subtract(new BigDecimal(sumExitDelegate==null?"0":sumExitDelegate)).toString();
			stakingStatisticNewResp.setStakingDelegationValue(stakingDelegationValue);
		}
		return stakingStatisticNewResp;
	}

	@Override
	public RespPage<AliveStakingListResp> aliveStakingList(AliveStakingListReq req) {
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		Integer status = null;
		Integer isConsensus = null;
		Integer isSetting = null;
		String name = req.getKey();
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
				isSetting = CustomStaking.YesNoEnum.YES.getCode();
				break;
			case CANDIDATE:
				/** 查询候选人 */
				status = StakingStatusEnum.CANDIDATE.getCode();
				isSetting = CustomStaking.YesNoEnum.NO.getCode();
				break;
			default:
				break;
		}

		RespPage<AliveStakingListResp> respPage = new RespPage<>();
		List<AliveStakingListResp> lists = new LinkedList<>();
		/** 根据条件和状态进行查询列表 */
		Page<StakingNode> stakingPage = customStakingMapper.selectStakingAndNodeByExample(null, name, status, isConsensus,isSetting);
		List<StakingNode> stakings = stakingPage.getResult();
		/** 查询出块节点 */
		NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
		for (int i = 0; i < stakings.size(); i++) {
			AliveStakingListResp aliveStakingListResp = new AliveStakingListResp();
			BeanUtils.copyProperties(stakings.get(i), aliveStakingListResp);
			aliveStakingListResp.setBlockQty(stakings.get(i).getStatBlockQty());
			aliveStakingListResp.setDelegateQty(stakings.get(i).getStatDelegateQty());
			/** 委托总金额数=委托交易总金额(犹豫期金额)+委托交易总金额(锁定期金额) */
			String sumAmount = new BigDecimal(stakings.get(i).getStatDelegateHas())
					.add(new BigDecimal(stakings.get(i).getStatDelegateLocked())).toString();
			aliveStakingListResp.setDelegateValue(sumAmount);
			aliveStakingListResp.setIsInit(stakings.get(i).getIsInit() == 1);
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
				aliveStakingListResp.setStatus(StakingStatusEnum.getCodeByStatus(stakings.get(i).getStatus(), stakings.get(i).getIsConsensus(), stakings.get(i).getIsSetting()));
			}
			/** 设置名称 */
			aliveStakingListResp.setNodeName(stakings.get(i).getStakingName());
			/** 质押总数=有效的质押+委托 */
			String totalValue = new BigDecimal(stakings.get(i).getStakingHas()).add(new BigDecimal(stakings.get(i).getStakingLocked()))
					.add(new BigDecimal(stakings.get(i).getStatDelegateHas())).add(new BigDecimal(stakings.get(i).getStatDelegateLocked())).toString();
			aliveStakingListResp.setTotalValue(totalValue);
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
		Page<StakingNode> stakings = customStakingMapper.selectHistoryNode(req.getKey(), status);
		for (StakingNode stakingNode:stakings.getResult()) {
			HistoryStakingListResp historyStakingListResp = new HistoryStakingListResp();
			BeanUtils.copyProperties(stakingNode, historyStakingListResp);
			if(stakingNode.getLeaveTime()!=null) {
				historyStakingListResp.setLeaveTime(stakingNode.getLeaveTime().getTime());
			}
			historyStakingListResp.setBlockQty(stakingNode.getCurConsBlockQty());
			historyStakingListResp.setNodeName(stakingNode.getStakingName());
			historyStakingListResp.setSlashLowQty(stakingNode.getStatSlashLowQty());
			historyStakingListResp.setSlashMultiQty(stakingNode.getStatSlashMultiQty());
			/**
			 * 带提取的委托等于hes+lock
			 */
			historyStakingListResp.setStatDelegateReduction(stakingNode.getAllDelegate());
			historyStakingListResp.setStatus(StakingStatusEnum.getCodeByStatus(stakingNode.getStatus(), stakingNode.getIsConsensus(), stakingNode.getIsSetting()));
			historyStakingListResp.setBlockQty(stakingNode.getStatBlockQty());
			lists.add(historyStakingListResp);
		}
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		page.setTotal(stakings.getTotal());
		respPage.init(page, lists);
		return respPage;
	}

	@Override
	public BaseResp<StakingChangeNewResp> stakingChangeNew() {
		StakingChangeNewResp stakingChangeNewResp = new StakingChangeNewResp();
		return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), stakingChangeNewResp);
	}

	@Override
	public BaseResp<StakingDetailsResp> stakingDetails(StakingDetailsReq req) {
		/** 补充前缀 */
		String nodeId = req.getNodeId();
		if(!req.getNodeId().startsWith(BrowserConst.WALLET_PRX)) {
			nodeId = BrowserConst.WALLET_PRX + nodeId ;
		}
		boolean isHistory = false;
		/**
		 * 先查询是否活跃节点，查不到再查询是否历史汇总
		 */
		List<StakingNode> stakings = customStakingMapper.selectStakingAndNodeActive(nodeId, StatusEnum.CANDIDATE.getCode());
		if(stakings == null || stakings.isEmpty()) {
			/**
			 * 历史详情汇总查询
			 */
			stakings = customStakingMapper.selectStakingAndNodeByNodeId(nodeId);
			isHistory = true;
		} 
		StakingDetailsResp resp = new StakingDetailsResp();
		// 只有一条数据
		if (!stakings.isEmpty()) {
			StakingNode stakingNode = stakings.get(0);
			BeanUtils.copyProperties(stakingNode, resp);
			resp.setIsInit(stakingNode.getIsInit() == 1);
			resp.setNodeName(stakingNode.getStakingName());
			resp.setStatus(StakingStatusEnum.getCodeByStatus(stakingNode.getStatus(), stakingNode.getIsConsensus(), stakingNode.getIsSetting()));
			resp.setDelegateQty(stakingNode.getStatDelegateQty());
			resp.setSlashLowQty(stakingNode.getStatSlashLowQty());
			resp.setSlashMultiQty(stakingNode.getStatSlashMultiQty());
			resp.setBlockQty(stakingNode.getStatBlockQty());
			resp.setExpectBlockQty(stakingNode.getStatExpectBlockQty());
			resp.setVerifierTime(stakingNode.getNodeStatVerifierTime());
			resp.setJoinTime(stakingNode.getJoinTime().getTime());
			/** 只有不是内置节点才计算年化率  */
			if (CustomStaking.YesNoEnum.YES.getCode() != stakingNode.getIsInit()) {
				resp.setExpectedIncome(stakingNode.getExpectedIncome());
				resp.setRewardValue("");
			} else {
				resp.setRewardValue(stakingNode.getFeeRewardValue());
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
			}
			if (stakingNode.getLeaveTime() != null) {
				resp.setLeaveTime(stakingNode.getLeaveTime().getTime());
			}
			/**
			 * 如果判断为true则表示为查历史数据
			 * 没有值则标识查询活跃账户
			 */
			if(isHistory) {
				resp.setDelegateQty(stakingNode.getAllDelegateQty());
				resp.setDelegateValue(stakingNode.getAllDelegate());
				resp.setStakingValue(stakingNode.getStakingReduction());
				String totalValue = new BigDecimal(resp.getStakingValue()).add(new BigDecimal(resp.getDelegateValue())).toString();
				resp.setTotalValue(totalValue);
				resp.setStatDelegateReduction(new BigDecimal(resp.getDelegateValue()).add(new BigDecimal(stakingNode.getStatDelegateReduction())).toString());
			} else {
				String delegateValue= new BigDecimal(stakingNode.getStatDelegateHas()).add(new BigDecimal(stakingNode.getStatDelegateLocked())).toString();
				/** 候选中则返回单条委托的总金额 **/
				resp.setDelegateValue(delegateValue);
				/** 质押金额=质押（犹豫期）+ 质押（锁定期）  */
				String stakingValue = new BigDecimal(stakingNode.getStakingHas()).add(new BigDecimal(stakingNode.getStakingLocked())).toString();
				resp.setStakingValue(stakingValue);
				/** 质押总数=有效的质押+委托 */
				String totalValue = new BigDecimal(stakingValue).add(new BigDecimal(delegateValue)).toString();
				resp.setTotalValue(totalValue);
			}
		}
		return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), resp);
	}
	
	@Override
	public RespPage<StakingOptRecordListResp> stakingOptRecordList( StakingOptRecordListReq req) {
		NodeOptExample nodeOptExample = new NodeOptExample();
		nodeOptExample.setOrderByClause(" id desc");
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		NodeOptExample.Criteria criteria = nodeOptExample.createCriteria();
		criteria.andNodeIdEqualTo(req.getNodeId());
		RespPage<StakingOptRecordListResp> respPage = new RespPage<>();
		List<StakingOptRecordListResp> lists = new LinkedList<>();
		/** 根据节点id查询节点操作记录 */
		List<NodeOpt> nodeOpts = nodeOptMapper.selectByExample(nodeOptExample);
		for (NodeOpt nodeOpt: nodeOpts) {
			StakingOptRecordListResp stakingOptRecordListResp = new StakingOptRecordListResp();
			BeanUtils.copyProperties(nodeOpt, stakingOptRecordListResp);
			stakingOptRecordListResp.setTimestamp(nodeOpt.getTimestamp().getTime());
			if(StringUtils.isNotBlank(nodeOpt.getDesc())) {
				String[] desces = nodeOpt.getDesc().split(BrowserConst.OPT_SPILT);
				/** 根据不同类型组合返回 */
				switch (CustomNodeOpt.TypeEnum.getEnum(nodeOpt.getType())) {
					/** 提案类型 */
					case PROPOSALS:
						stakingOptRecordListResp.setId(BrowserConst.PIP_NAME + desces[0]);
						stakingOptRecordListResp.setTitle("inquiry".equals(desces[1])?"":desces[1]);
						stakingOptRecordListResp.setProposalType(desces[2]);
						if(desces.length > 3) {
							stakingOptRecordListResp.setVersion(desces[3]);
						}
						break;
					/** 投票类型 */
					case VOTE:
						stakingOptRecordListResp.setId(BrowserConst.PIP_NAME + desces[0]);
						stakingOptRecordListResp.setTitle("inquiry".equals(desces[1])?"":desces[1]);
						stakingOptRecordListResp.setOption(desces[2]);
						stakingOptRecordListResp.setProposalType(desces[3]);
						if(desces.length > 4) {
							stakingOptRecordListResp.setVersion(desces[4]);
						}
						break;
					/** 双签 */
					case MULTI_SIGN:
						stakingOptRecordListResp.setPercent(desces[0]);
						stakingOptRecordListResp.setAmount(desces[1]);
						break;
					/** 出块率低 */
					case LOW_BLOCK_RATE:
						stakingOptRecordListResp.setPercent(desces[0]);
						stakingOptRecordListResp.setAmount(desces[2]);
						stakingOptRecordListResp.setIsFire(Integer.parseInt(desces[3]));
						break;
					default:
						break;
				}
			}
			
			lists.add(stakingOptRecordListResp);
		}
		/** 查询分页总数 */
		long size = nodeOptMapper.countByExample(nodeOptExample);
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		page.setTotal(size);
		respPage.init(page, lists);
		return respPage;
	}
	
	@Override
	public RespPage<DelegationListByStakingResp> delegationListByStaking( DelegationListByStakingReq req) {
		StakingKey stakingKey = new StakingKey();
		stakingKey.setNodeId(req.getNodeId());
		stakingKey.setStakingBlockNum(Long.valueOf(req.getStakingBlockNum()));
		Staking staking = stakingMapper.selectByPrimaryKey(stakingKey);
		Long num = Long.valueOf(req.getStakingBlockNum());
		/**
		 * 如果验证人状态不为候选中则无需查询总数
		 */
		if(staking !=  null && StatusEnum.CANDIDATE.getCode() != staking.getStatus().intValue()) {
			num = null;
		}
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		List<DelegationListByStakingResp> lists = new LinkedList<>();
		/** 根据节点id和区块查询验证委托信息 */
		Page<DelegationStaking> delegationStakings = 
				customDelegationMapper.selectStakingByExample(req.getNodeId(),num );
		List<DelegationStaking> sumDelegationStaking = customDelegationMapper.selectSumDelegateByExample(req.getNodeId(),num);
		String allDelegate = "0";
		String allLockDelegate = "0";
		if( sumDelegationStaking.get(0) != null) {
			allDelegate = sumDelegationStaking.get(0).getAllDelegate();
			allLockDelegate = sumDelegationStaking.get(0).getAllLockDelegate();
		}
		for (DelegationStaking delegationStaking: delegationStakings.getResult()) {
			DelegationListByStakingResp byStakingResp = new DelegationListByStakingResp();
			byStakingResp.setDelegateAddr(delegationStaking.getDelegateAddr());
		   /**已锁定委托（LAT）如果关联的验证人状态正常则正常显示，如果其他情况则为零（delegation）  */
			String deleLock = delegationStaking.getStatus()==CustomStaking.StatusEnum.CANDIDATE.getCode()?delegationStaking.getDelegateLocked():"0";
			byStakingResp.setDelegateLocked(deleLock);
			/**
			 * 委托金额等于has加上实际lock金额
			 */
			String delValue = new BigDecimal(delegationStaking.getDelegateHas())
					.add(new BigDecimal(delegationStaking.getDelegateLocked())).toString();
			byStakingResp.setDelegateValue(delValue);
			byStakingResp.setAllDelegateLocked(allLockDelegate);
			byStakingResp.setDelegateTotalValue(allDelegate);
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
		Page<DelegationStaking> delegationStakings = 
				customDelegationMapper.selectDelegationByExample(req.getAddress());
		
		Address item = addressMapper.selectByPrimaryKey(req.getAddress());
		for (DelegationStaking delegationStaking:  delegationStakings.getResult()) {
			DelegationListByAddressResp byAddressResp = new DelegationListByAddressResp();
			BeanUtils.copyProperties(delegationStaking, byAddressResp);
			byAddressResp.setNodeName(delegationStaking.getStakingName());
			/** 如果状态等于候选中则正常显示，否则为0 */
			String deletgateHas = delegationStaking.getStatus()==CustomStaking.StatusEnum.CANDIDATE.getCode()?delegationStaking.getDelegateHas():"0";
			byAddressResp.setDelegateHas(deletgateHas);
			/** 如果关联的验证人状态正常则正常显示，如果其他情况则为零（delegation） */
			String deletgateLock = delegationStaking.getStatus()==CustomStaking.StatusEnum.CANDIDATE.getCode()?delegationStaking.getDelegateLocked():"0";
			byAddressResp.setDelegateLocked(deletgateLock);
			/** 委托金额=犹豫期金额加上锁定期金额 */
			String deleValue = new BigDecimal(delegationStaking.getDelegateHas())
					.add(new BigDecimal(byAddressResp.getDelegateLocked())).toString();
			byAddressResp.setDelegateValue(deleValue);
			/** 如果关联的验证人状态退出中或已退出则为delegateHas+delegateLocked，如果其他情况则为0（delegation） */
			String deletgateUnLock = delegationStaking.getStatus()==CustomStaking.StatusEnum.CANDIDATE.getCode()?"0":new BigDecimal(delegationStaking.getDelegateHas())
					.add(new BigDecimal(delegationStaking.getDelegateLocked())).toString() ;
			byAddressResp.setDelegateUnlock(deletgateUnLock);
			byAddressResp.setDelegateTotalValue(item.getDelegateUnlock());
			byAddressResp.setAllDelegateLocked(item.getDelegateLocked());
			lists.add(byAddressResp);
		}
		/** 统计总数 */
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		page.setTotal(delegationStakings.getTotal());
		RespPage<DelegationListByAddressResp> respPage = new RespPage<>();
		respPage.init(page, lists);
		return respPage;
	}

}
