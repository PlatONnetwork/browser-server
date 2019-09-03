package com.platon.browser.now.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.common.BrowserConst;
import com.platon.browser.dao.entity.DelegationExample;
import com.platon.browser.dao.entity.DelegationStaking;
import com.platon.browser.dao.entity.NetworkStat;
import com.platon.browser.dao.entity.NodeOpt;
import com.platon.browser.dao.entity.NodeOptExample;
import com.platon.browser.dao.entity.StakingNode;
import com.platon.browser.dao.mapper.CustomStakingMapper;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.dao.mapper.NodeOptMapper;
import com.platon.browser.dto.CustomStaking;
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
	private NodeOptMapper nodeOptMapper;
	
	@Autowired
	private DelegationMapper delegationMapper;

	@Autowired
	private I18nUtil i18n;
	
	@Override
	public StakingStatisticNewResp stakingStatisticNew() {
		/** 获取统计信息 */
		NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
		StakingStatisticNewResp stakingStatisticNewResp = new StakingStatisticNewResp();
		if(networkStatRedis != null) {
			BeanUtils.copyProperties(networkStatRedis, stakingStatisticNewResp);
		}
		return stakingStatisticNewResp;
	}

	@Override
	public RespPage<AliveStakingListResp> aliveStakingList(AliveStakingListReq req) {
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		Integer status = null;
		Integer isConsensus = null;
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
				/** 活跃中代表即使后续同时也是共识周期验证人 */
				status = StakingStatusEnum.CANDIDATE.getCode();
				isConsensus = CustomStaking.YesNoEnum.YES.getCode();
				break;
			case CANDIDATE:
				/** 查询候选人 */
				status = StakingStatusEnum.CANDIDATE.getCode();
				isConsensus = CustomStaking.YesNoEnum.NO.getCode();
				break;
			default:
				break;
		}

		RespPage<AliveStakingListResp> respPage = new RespPage<>();
		List<AliveStakingListResp> lists = new LinkedList<AliveStakingListResp>();
		/** 根据条件和状态进行查询列表 */
		Page<StakingNode> stakingPage = customStakingMapper.selectStakingAndNodeByExample(null, name, status, isConsensus);
		List<StakingNode> stakings = stakingPage.getResult();
		/** 查询出块节点 */
		NetworkStat networkStatRedis = statisticCacheService.getNetworkStatCache();
		for (int i = 0; i < stakings.size(); i++) {
			AliveStakingListResp aliveStakingListResp = new AliveStakingListResp();
			BeanUtils.copyProperties(stakings.get(i), aliveStakingListResp);
			aliveStakingListResp.setBlockQty(stakings.get(i).getCurConsBlockQty());
			aliveStakingListResp.setDelegateQty(stakings.get(i).getStatDelegateQty());
			/** 委托总金额数=委托交易总金额(犹豫期金额)+委托交易总金额(锁定期金额) */
			String sumAmount = new BigDecimal(stakings.get(i).getStatDelegateHas())
					.add(new BigDecimal(stakings.get(i).getStatDelegateLocked())).toString();
			aliveStakingListResp.setDelegateValue(sumAmount);
			aliveStakingListResp.setIsInit(stakings.get(i).getIsInit().intValue() == 1?true:false);
			if(stakings.get(i).getIsRecommend() != null) {
				aliveStakingListResp.setIsRecommend(Integer.valueOf(1).compareTo(stakings.get(i).getIsRecommend()) == 0?true:false);
			}
			/** 设置排行 */
			aliveStakingListResp.setRanking(i + 1);
			aliveStakingListResp.setSlashLowQty(stakings.get(i).getStatSlashLowQty());
			aliveStakingListResp.setSlashMultiQty(stakings.get(i).getStatSlashMultiQty());
			/** 如果是对应的出块节点则置为出块中，否则为活跃中或者退出 */
			if(stakings.get(i).getNodeId().equals(networkStatRedis.getNodeId())) {
				aliveStakingListResp.setStatus(StakingStatusEnum.BLOCK.getCode());
			} else {
				aliveStakingListResp.setStatus(StakingStatusEnum.getCodeByStatus(stakings.get(i).getStatus(), stakings.get(i).getIsConsensus()));
			}
			
			aliveStakingListResp.setNodeName(stakings.get(i).getStakingName());
			/* 质押总数=有效的质押+委托 */
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
		List<Integer> status = new ArrayList<Integer>();
		status.add(CustomStaking.StatusEnum.EXITING.getCode());
		status.add(CustomStaking.StatusEnum.EXITED.getCode());
		RespPage<HistoryStakingListResp> respPage = new RespPage<>();
		List<HistoryStakingListResp> lists = new LinkedList<HistoryStakingListResp>();
		/** 根据条件和状态进行查询列表 */
		Page<StakingNode> stakings = customStakingMapper.selectHistoryNode(status);
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
			 * 带提取的委托等于has+lock
			 */
			String totalValue = new BigDecimal(stakingNode.getStatDelegateHas()).add(new BigDecimal(stakingNode.getStatDelegateLocked())).toString();
			historyStakingListResp.setStatDelegateReduction(totalValue);
			historyStakingListResp.setStatus(StakingStatusEnum.getCodeByStatus(stakingNode.getStatus(), stakingNode.getIsConsensus()));
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
		List<StakingNode> stakings = customStakingMapper.selectStakingAndNodeByExample(req.getNodeId(),null ,null, null);
		Integer size = stakings.size();
		StakingDetailsResp resp = new StakingDetailsResp();
		switch (size) {
			case 0:
				break;
			default: // 只有一条数据
				StakingNode stakingNode = stakings.get(0);
				BeanUtils.copyProperties(stakingNode, resp);
				resp.setIsInit(stakingNode.getIsInit().intValue() == 1?true:false);
				resp.setNodeName(stakingNode.getStakingName());
				resp.setStatus(StakingStatusEnum.getCodeByStatus(stakingNode.getStatus(), stakingNode.getIsConsensus()));
				/** 质押总数=有效的质押+委托 */
				String totalValue = new BigDecimal(stakingNode.getStakingHas()).add(new BigDecimal(stakingNode.getStakingLocked()))
						.add(new BigDecimal(stakingNode.getStatDelegateHas())).add(new BigDecimal(stakingNode.getStatDelegateLocked())).toString();
				resp.setTotalValue(totalValue);
				/** 委托总金额数=委托交易总金额(犹豫期金额)+委托交易总金额(锁定期金额) */
				String delValue = new BigDecimal(stakingNode.getStatDelegateHas()).add(new BigDecimal(stakingNode.getStatDelegateLocked())).toString();
				resp.setDelegateValue(delValue);
				/** 质押金额=质押（犹豫期）+ 质押（锁定期）  */
				String stakingValue = new BigDecimal(stakingNode.getStakingHas()).add(new BigDecimal(stakingNode.getStakingLocked())).toString();
				resp.setStakingValue(stakingValue);
				resp.setDelegateQty(stakingNode.getStatDelegateQty());
				resp.setSlashLowQty(stakingNode.getStatSlashLowQty());
				resp.setSlashMultiQty(stakingNode.getStatSlashMultiQty());
				resp.setBlockQty(stakingNode.getStatBlockQty());
				resp.setExpectBlockQty(stakingNode.getStatExpectBlockQty());
				String webSite = "";
				if(StringUtils.isNotBlank(stakingNode.getWebSite()) ) {
					/**
					 * 如果地址不是http开头就补齐
					 */
					if(stakingNode.getWebSite().startsWith(BrowserConst.HTTP) || stakingNode.getWebSite().startsWith(BrowserConst.HTTPS)){
						webSite = stakingNode.getWebSite();
					} else {
						webSite = BrowserConst.HTTP + stakingNode.getWebSite();
					}
				}
				resp.setWebsite(webSite);
				/** 实际跳转地址是url拼接上名称 */
				if(StringUtils.isNotBlank(stakingNode.getExternalName())) {
					resp.setExternalUrl(BrowserConst.EX_URL + stakingNode.getExternalName());
				}
				resp.setVerifierTime(stakingNode.getStatVerifierTime());
				resp.setJoinTime(stakingNode.getJoinTime().getTime());
				if(StringUtils.isNotBlank(stakingNode.getStatRewardValue())) {
					resp.setRewardValue(stakingNode.getStatRewardValue());
				}
				if(stakingNode.getLeaveTime() != null) {
					resp.setLeaveTime(stakingNode.getLeaveTime().getTime());
				}
				break;
		}
		return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), resp);
	}
	
	@Override
	public RespPage<StakingOptRecordListResp> stakingOptRecordList( StakingOptRecordListReq req) {
		NodeOptExample nodeOptExample = new NodeOptExample();
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		NodeOptExample.Criteria criteria = nodeOptExample.createCriteria();
		criteria.andNodeIdEqualTo(req.getNodeId());
		RespPage<StakingOptRecordListResp> respPage = new RespPage<>();
		List<StakingOptRecordListResp> lists = new LinkedList<StakingOptRecordListResp>();
		/** 根据节点id查询节点操作记录 */
		List<NodeOpt> nodeOpts = nodeOptMapper.selectByExample(nodeOptExample);
		for (NodeOpt nodeOpt: nodeOpts) {
			StakingOptRecordListResp stakingOptRecordListResp = new StakingOptRecordListResp();
			BeanUtils.copyProperties(nodeOpt, stakingOptRecordListResp);
			stakingOptRecordListResp.setTimestamp(nodeOpt.getCreateTime().getTime());
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
		DelegationExample delegationExample = new DelegationExample();
		DelegationExample.Criteria criteria = delegationExample.createCriteria();
		criteria.andNodeIdEqualTo(req.getNodeId());
		criteria.andStakingBlockNumEqualTo(Long.parseLong(req.getStakingBlockNum()));
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		List<DelegationListByStakingResp> lists = new LinkedList<DelegationListByStakingResp>();
		/** 根据节点id和区块查询验证委托信息 */
		List<DelegationStaking> delegationStakings = 
				delegationMapper.selectDelegationAndStakingByExample(req.getNodeId(),Long.parseLong(req.getStakingBlockNum()),null);
		String allDelegate = "0";
		String allLockDelegate = "0";
		for (DelegationStaking delegationStaking: delegationStakings) {
			DelegationListByStakingResp byStakingResp = new DelegationListByStakingResp();
			byStakingResp.setDelegateAddr(delegationStaking.getDelegateAddr());
			String delValue = new BigDecimal(delegationStaking.getDelegateHas())
					.add(new BigDecimal(delegationStaking.getDelegateLocked())).toString();
			byStakingResp.setDelegateValue(delValue);
		   /**已锁定委托（LAT）如果关联的验证人状态正常则正常显示，如果其他情况则为零（delegation）  */
			String deleLock = delegationStaking.getStatus()==2?delegationStaking.getDelegateLocked():"0";
			byStakingResp.setDelegateLocked(deleLock);
			/** 总质押金额累加 */
			allDelegate = new BigDecimal(allDelegate).add(new BigDecimal(delValue).add(new BigDecimal(deleLock))).toString();
			/** 总质押锁定金额累积 */
			allLockDelegate = new BigDecimal(allLockDelegate).add(new BigDecimal(deleLock)).toString();
			byStakingResp.setAllDelegateLocked(allDelegate);
			byStakingResp.setDelegateTotalValue(allDelegate);
			lists.add(byStakingResp);
		}
		/** 分页统计总数 */
		long size = delegationMapper.countByExample(delegationExample);
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		page.setTotal(size);
		RespPage<DelegationListByStakingResp> respPage = new RespPage<>();
		respPage.init(page, lists);
		return respPage;
	}
	
	@Override
	public RespPage<DelegationListByAddressResp> delegationListByAddress( DelegationListByAddressReq req) {
		DelegationExample delegationExample = new DelegationExample();
		com.platon.browser.dao.entity.DelegationExample.Criteria criteria = delegationExample.createCriteria();
		criteria.andDelegateAddrEqualTo(req.getAddress());
		
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		List<DelegationListByAddressResp> lists = new LinkedList<DelegationListByAddressResp>();
		/** 根据地址分页查询委托列表 */
		List<DelegationStaking> delegationStakings = 
				delegationMapper.selectDelegationAndStakingByExample(null,null,req.getAddress());
		for (DelegationStaking delegationStaking:  delegationStakings) {
			DelegationListByAddressResp byAddressResp = new DelegationListByAddressResp();
			BeanUtils.copyProperties(delegationStaking, byAddressResp);
			byAddressResp.setNodeName(delegationStaking.getStakingName());
			/** 委托金额=犹豫期金额加上锁定期金额 */
			String deleValue = new BigDecimal(delegationStaking.getDelegateHas())
					.add(new BigDecimal(delegationStaking.getDelegateLocked())).toString();
			byAddressResp.setDelegateValue(deleValue);
			byAddressResp.setAllDelegateLocked(delegationStaking.getStatDelegateLocked());
			/** 如果状态等于候选中则正常显示，否则为0 */
			String deletgateHas = delegationStaking.getStatus()==CustomStaking.StatusEnum.CANDIDATE.getCode()?delegationStaking.getDelegateHas():"0";
			byAddressResp.setDelegateHas(deletgateHas);
			String deletgateLock = delegationStaking.getStatus()==CustomStaking.StatusEnum.CANDIDATE.getCode()?delegationStaking.getDelegateLocked():"0";
			byAddressResp.setDelegateLocked(deletgateLock);
			String deletgateUnLock = delegationStaking.getStatus()==CustomStaking.StatusEnum.CANDIDATE.getCode()?new BigDecimal(delegationStaking.getDelegateHas())
					.add(new BigDecimal(delegationStaking.getDelegateLocked())).toString() :"0";
			byAddressResp.setDelegateUnlock(deletgateUnLock);
			lists.add(byAddressResp);
		}
		/** 统计总数 */
		long size = delegationMapper.countByExample(delegationExample);
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		page.setTotal(size);
		RespPage<DelegationListByAddressResp> respPage = new RespPage<>();
		respPage.init(page, lists);
		return respPage;
	}

}
