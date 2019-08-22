package com.platon.browser.now.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.utils.Convert;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.dao.entity.DelegationExample;
import com.platon.browser.dao.entity.DelegationStaking;
import com.platon.browser.dao.entity.NodeOpt;
import com.platon.browser.dao.entity.NodeOptExample;
import com.platon.browser.dao.entity.StakingExample;
import com.platon.browser.dao.entity.StakingExample.Criteria;
import com.platon.browser.dao.entity.StakingNode;
import com.platon.browser.dao.mapper.CustomStakingMapper;
import com.platon.browser.dao.mapper.DelegationMapper;
import com.platon.browser.dao.mapper.NodeOptMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.IsConsensusStatus;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.enums.StakingStatus;
import com.platon.browser.enums.StakingStatusEnum;
import com.platon.browser.now.service.StakingService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.redis.dto.NetworkStatRedis;
import com.platon.browser.req.staking.AliveStakingListReq;
import com.platon.browser.req.staking.DelegationListByAddressReq;
import com.platon.browser.req.staking.DelegationListByStakingReq;
import com.platon.browser.req.staking.HistoryStakingListReq;
import com.platon.browser.req.staking.StakingDetailsReq;
import com.platon.browser.req.staking.StakingOptRecordListReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.resp.staking.AliveStakingListResp;
import com.platon.browser.resp.staking.DelegationListByAddressResp;
import com.platon.browser.resp.staking.DelegationListByStakingResp;
import com.platon.browser.resp.staking.HistoryStakingListResp;
import com.platon.browser.resp.staking.StakingChangeNewResp;
import com.platon.browser.resp.staking.StakingDetailsResp;
import com.platon.browser.resp.staking.StakingOptRecordListResp;
import com.platon.browser.resp.staking.StakingStatisticNewResp;
import com.platon.browser.util.EnergonUtil;
import com.platon.browser.util.I18nUtil;

@Service
public class StakingServiceImpl implements StakingService {

	@Autowired
	private StatisticCacheService statisticCacheService;

	@Autowired
	private StakingMapper stakingMapper;

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
		NetworkStatRedis networkStatRedis = statisticCacheService.getNetworkStatCache();
		StakingStatisticNewResp stakingStatisticNewResp = new StakingStatisticNewResp();
		if(networkStatRedis != null) {
			stakingStatisticNewResp.setAddIssueBegin(networkStatRedis.getAddIssueBegin());
			stakingStatisticNewResp.setAddIssueEnd(networkStatRedis.getAddIssueEnd());
			stakingStatisticNewResp.setBlockReward(EnergonUtil.format(Convert.fromVon(networkStatRedis.getBlockReward(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
			stakingStatisticNewResp.setCurrentNumber(networkStatRedis.getCurrentNumber());
			stakingStatisticNewResp.setIssueValue(networkStatRedis.getIssueValue());
			stakingStatisticNewResp.setNextSetting(networkStatRedis.getNextSetting());
			stakingStatisticNewResp.setStakingDelegationValue(EnergonUtil.format(Convert.fromVon(networkStatRedis.getStakingDelegationValue(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
			stakingStatisticNewResp.setStakingReward(EnergonUtil.format(Convert.fromVon(networkStatRedis.getStakingReward(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
			stakingStatisticNewResp.setStakingValue(networkStatRedis.getStakingValue());
		}
		return stakingStatisticNewResp;
	}

	@Override
	public RespPage<AliveStakingListResp> aliveStakingList(AliveStakingListReq req) {
		StakingExample stakingExample = new StakingExample();
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		Integer status = null;
		Integer isConsensus = null;
		Criteria criteria = stakingExample.createCriteria();
		if(StringUtils.isNotBlank(req.getKey())) {
			criteria.andStakingNameEqualTo(req.getKey());
		}
		switch (StakingStatusEnum.valueOf(req.getQueryStatus().toUpperCase())) {
			case ALL:
				break;
			case ACTIVE:
				//活跃中代表即使后续同时也是共识周期验证人
				status = StakingStatus.CANDIDATE.getCode();
				isConsensus = IsConsensusStatus.YES.getCode();
				criteria.andStatusEqualTo(StakingStatus.CANDIDATE.getCode()).andIsConsensusEqualTo(IsConsensusStatus.YES.getCode());
				break;
			case CANDIDATE:
				status = StakingStatus.CANDIDATE.getCode();
				criteria.andStatusEqualTo(StakingStatus.CANDIDATE.getCode());
				break;
			default:
				break;
		}

		RespPage<AliveStakingListResp> respPage = new RespPage<>();
		List<AliveStakingListResp> lists = new LinkedList<AliveStakingListResp>();
		//根据条件和状态进行查询列表
		List<StakingNode> stakings = customStakingMapper.selectStakingAndNodeByExample(null, req.getKey(), status, isConsensus);
		for (int i = 0; i < stakings.size(); i++) {
			AliveStakingListResp aliveStakingListResp = new AliveStakingListResp();
			aliveStakingListResp.setBlockQty(stakings.get(i).getCurConsBlockQty());
			aliveStakingListResp.setDelegateQty(stakings.get(i).getStatDelegateQty());
			//委托总金额数=委托交易总金额(犹豫期金额)+委托交易总金额(锁定期金额)
			String sumAmount = new BigDecimal(stakings.get(i).getStatDelegateHas())
					.add(new BigDecimal(stakings.get(i).getStatDelegateLocked())).toString();
			aliveStakingListResp.setDelegateValue(EnergonUtil.format(Convert.fromVon(sumAmount, Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
			aliveStakingListResp.setExpectedIncome(stakings.get(i).getExpectedIncome());
			aliveStakingListResp.setIsInit(stakings.get(i).getIsInit().intValue() == 1?true:false);
			aliveStakingListResp.setIsRecommend(stakings.get(i).getIsRecommend().intValue() == 1?true:false);
			aliveStakingListResp.setNodeId(stakings.get(i).getNodeId());
			aliveStakingListResp.setNodeName(stakings.get(i).getStakingName());
			aliveStakingListResp.setRanking(i + 1);
			aliveStakingListResp.setSlashLowQty(stakings.get(i).getStatSlashLowQty());
			aliveStakingListResp.setSlashMultiQty(stakings.get(i).getStatSlashMultiQty());
			aliveStakingListResp.setStakingIcon(stakings.get(i).getStakingIcon());
			aliveStakingListResp.setStatus(StakingStatusEnum.getCodeByStatus(stakings.get(i).getStatus(), stakings.get(i).getIsConsensus()));
			//质押总数=有效的质押+委托
			String totalValue = new BigDecimal(stakings.get(i).getStakingHas()).add(new BigDecimal(stakings.get(i).getStakingLocked()))
					.add(new BigDecimal(stakings.get(i).getStatDelegateHas())).add(new BigDecimal(stakings.get(i).getStatDelegateLocked())).toString();
			aliveStakingListResp.setTotalValue(EnergonUtil.format(Convert.fromVon(totalValue, Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
			lists.add(aliveStakingListResp);
		}
		long size = stakingMapper.countByExample(stakingExample);
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		page.setTotal(size);
		respPage.init(page, lists);
		return respPage;
	}

	@Override
	public RespPage<HistoryStakingListResp> historyStakingList(HistoryStakingListReq req) {
		StakingExample stakingExample = new StakingExample();
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		Criteria criteria = stakingExample.createCriteria();
		if(StringUtils.isNotBlank(req.getKey())) {
			criteria.andStakingNameEqualTo(req.getKey());
		}
		RespPage<HistoryStakingListResp> respPage = new RespPage<>();
		List<HistoryStakingListResp> lists = new LinkedList<HistoryStakingListResp>();
		//根据条件和状态进行查询列表
		List<StakingNode> stakings = customStakingMapper.selectStakingAndNodeByExample(null, req.getKey(), null, null);
		for (StakingNode stakingNode:stakings) {
			HistoryStakingListResp historyStakingListResp = new HistoryStakingListResp();
			historyStakingListResp.setLeaveTime(stakingNode.getLeaveTime().getTime());
			historyStakingListResp.setNodeId(stakingNode.getNodeId());
			historyStakingListResp.setNodeName(stakingNode.getStakingName());
			historyStakingListResp.setSlashLowQty(stakingNode.getStatSlashLowQty());
			historyStakingListResp.setSlashMultiQty(stakingNode.getStatSlashMultiQty());
			historyStakingListResp.setStakingIcon(stakingNode.getStakingIcon());
			historyStakingListResp.setStatDelegateReduction(stakingNode.getStatDelegateReduction());
			historyStakingListResp.setStatus(StakingStatusEnum.getCodeByStatus(stakingNode.getStatus(), stakingNode.getIsConsensus()));
			lists.add(historyStakingListResp);
		}
		long size = stakingMapper.countByExample(stakingExample);
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		page.setTotal(size);
		respPage.init(page, lists);
		return respPage;
	}

	@Override
	public BaseResp<StakingChangeNewResp> stakingChangeNew() {

		return null;
	}

	@Override
	public BaseResp<StakingDetailsResp> stakingDetails(StakingDetailsReq req) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<StakingNode> stakings = customStakingMapper.selectStakingAndNodeByExample(req.getNodeId(),null ,null, null);
		Integer size = stakings.size();
		StakingDetailsResp resp = new StakingDetailsResp();
		switch (size) {
			case 0:
				// TODO 没数据
				break;
			case 1: // 只有一条数据
				StakingNode stakingNode = stakings.get(0);
				resp.setNodeName(stakingNode.getStakingName());
				resp.setStakingIcon(stakingNode.getStakingIcon());
				resp.setStatus(StakingStatus.getDescByCode(stakingNode.getStatus()));
				//质押总数=有效的质押+委托
				String totalValue = new BigDecimal(stakingNode.getStakingHas()).add(new BigDecimal(stakingNode.getStakingLocked()))
						.add(new BigDecimal(stakingNode.getStatDelegateHas())).add(new BigDecimal(stakingNode.getStatDelegateLocked())).toString();
				resp.setTotalValue(EnergonUtil.format(Convert.fromVon(totalValue, Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
				//委托总金额数=委托交易总金额(犹豫期金额)+委托交易总金额(锁定期金额)
				String delValue = new BigDecimal(stakingNode.getStatDelegateHas()).add(new BigDecimal(stakingNode.getStatDelegateLocked())).toString();
				resp.setDelegateValue(EnergonUtil.format(Convert.fromVon(delValue, Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
				String stakingValue = new BigDecimal(stakingNode.getStakingHas()).add(new BigDecimal(stakingNode.getStakingLocked())).toString();
				resp.setStakingValue(EnergonUtil.format(Convert.fromVon(stakingValue, Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
				resp.setDelegateQty(stakingNode.getStatDelegateQty());
				resp.setSlashLowQty(stakingNode.getStatSlashLowQty());
				resp.setSlashMultiQty(stakingNode.getStatSlashMultiQty());
				resp.setBlockQty(stakingNode.getStatBlockQty());
				resp.setExpectBlockQty(stakingNode.getStatExpectBlockQty());
				resp.setExpectedIncome(stakingNode.getExpectedIncome());
				resp.setJoinTime(sdf.format(stakingNode.getJoinTime()));
				resp.setVerifierTime(stakingNode.getStatVerifierTime());
				resp.setRewardValue(EnergonUtil.format(Convert.fromVon(stakingNode.getStatRewardValue(), Convert.Unit.LAT).setScale(18,RoundingMode.DOWN)));
				resp.setNodeId(stakingNode.getNodeId());
				resp.setStakingAddr(stakingNode.getStakingAddr());
				resp.setDenefitAddr(stakingNode.getDenefitAddr());
				resp.setWebsite(stakingNode.getWebSite());
				resp.setDetails(stakingNode.getDetails());
				resp.setExternalId(stakingNode.getExternalId());
				resp.setStakingBlockNum(Long.toString(stakingNode.getStakingBlockNum()));
				resp.setStatDelegateReduction(stakingNode.getStatDelegateReduction());
				break;
			default:
				// TODO 有多条数据就报错
				return BaseResp.build(RetEnum.RET_SYS_EXCEPTION.getCode(), i18n.i(I18nEnum.FAILURE), null);
		}
		return BaseResp.build(RetEnum.RET_SUCCESS.getCode(), i18n.i(I18nEnum.SUCCESS), resp);
	}
	
	@Override
	public RespPage<StakingOptRecordListResp> stakingOptRecordList( StakingOptRecordListReq req) {
		NodeOptExample nodeOptExample = new NodeOptExample();
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		com.platon.browser.dao.entity.NodeOptExample.Criteria criteria = nodeOptExample.createCriteria();
		criteria.andNodeIdEqualTo(req.getNodeId());
		RespPage<StakingOptRecordListResp> respPage = new RespPage<>();
		List<StakingOptRecordListResp> lists = new LinkedList<StakingOptRecordListResp>();
		List<NodeOpt> nodeOpts = nodeOptMapper.selectByExample(nodeOptExample);
		for (int i = 0; i<nodeOpts.size(); i++) {
			NodeOpt nodeOpt = nodeOpts.get(i);
			StakingOptRecordListResp stakingOptRecordListResp = new StakingOptRecordListResp();
			stakingOptRecordListResp.setBlockNumber(nodeOpt.getBlockNumber());
			stakingOptRecordListResp.setDesc(nodeOpt.getDesc());
			stakingOptRecordListResp.setTimestamp(nodeOpt.getCreateTime().getTime());
			stakingOptRecordListResp.setTxHash(nodeOpt.getTxHash());
			lists.add(stakingOptRecordListResp);
		}
		long size = nodeOptMapper.countByExample(nodeOptExample);
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		page.setTotal(size);
		respPage.init(page, lists);
		return respPage;
	}
	
	@Override
	public RespPage<DelegationListByStakingResp> delegationListByStaking( DelegationListByStakingReq req) {
		DelegationExample delegationExample = new DelegationExample();
		com.platon.browser.dao.entity.DelegationExample.Criteria criteria = delegationExample.createCriteria();
		criteria.andNodeIdEqualTo(req.getNodeId());
		criteria.andStakingBlockNumEqualTo(Long.parseLong(req.getStakingBlockNum()));
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		List<DelegationListByStakingResp> lists = new LinkedList<DelegationListByStakingResp>();
		
		List<DelegationStaking> delegationStakings = 
				delegationMapper.selectDelegationAndStakingByExample(req.getNodeId(),Long.parseLong(req.getStakingBlockNum()));
		for (int i = 0; i < delegationStakings.size(); i++) {
			DelegationStaking delegationStaking = delegationStakings.get(i);
			DelegationListByStakingResp byStakingResp = new DelegationListByStakingResp();
			byStakingResp.setDelegateAddr(delegationStaking.getDelegateAddr());
			byStakingResp.setDelegateValue(new BigDecimal(delegationStaking.getDelegateHas())
					.add(new BigDecimal(delegationStaking.getDelegateLocked())).toString());
			// "delegateLocked":"",    //已锁定委托（LAT）如果关联的验证人状态正常则正常显示，如果其他情况则为零（delegation）
			byStakingResp.setDelegateLocked(delegationStaking.getStatus()==2?delegationStaking.getDelegateLocked():"0");
			byStakingResp.setAllDelegateLocked(delegationStaking.getStatDelegateLocked());
			lists.add(byStakingResp);
		}
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
		
		List<DelegationStaking> delegationStakings = delegationMapper.selectDelegationAndStakingByExample(req.getAddress());
		for (int i = 0; i < delegationStakings.size(); i++) {
			DelegationStaking delegationStaking = delegationStakings.get(i);
			DelegationListByAddressResp byAddressResp = new DelegationListByAddressResp();
			byAddressResp.setNodeId(delegationStaking.getNodeId());
			byAddressResp.setNodeName(delegationStaking.getStakingName());
			byAddressResp.setDelegateValue(new BigDecimal(delegationStaking.getDelegateHas())
					.add(new BigDecimal(delegationStaking.getDelegateLocked())).toString());
			byAddressResp.setDelegateHas(delegationStaking.getStatus()==2?delegationStaking.getDelegateHas():"0");
			byAddressResp.setDelegateLocked(delegationStaking.getStatus()==2?delegationStaking.getDelegateLocked():"0");
			byAddressResp.setAllDelegateLocked(delegationStaking.getStatDelegateLocked());
			byAddressResp.setDelegateUnlock(delegationStaking.getStatus()==2?"0":new BigDecimal(delegationStaking.getDelegateHas())
					.add(new BigDecimal(delegationStaking.getDelegateLocked())).toString());
			byAddressResp.setDelegateReduction(delegationStaking.getDelegateReduction());
			lists.add(byAddressResp);
		}
		long size = delegationMapper.countByExample(delegationExample);
		Page<?> page = new Page<>(req.getPageNo(), req.getPageSize());
		page.setTotal(size);
		RespPage<DelegationListByAddressResp> respPage = new RespPage<>();
		respPage.init(page, lists);
		return respPage;
	}

}
