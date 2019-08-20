package com.platon.browser.now.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import com.platon.browser.dao.mapper.CustomStakingMapper;
import com.platon.browser.dao.mapper.StakingMapper;
import com.platon.browser.enums.*;
import com.platon.browser.req.staking.StakingDetailsReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.resp.staking.*;
import com.platon.browser.util.I18nUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.platon.browser.dao.entity.StakingExample;
import com.platon.browser.dao.entity.StakingExample.Criteria;
import com.platon.browser.dao.entity.StakingNode;
import com.platon.browser.dto.RespPage;
import com.platon.browser.now.service.StakingService;
import com.platon.browser.now.service.cache.StatisticCacheService;
import com.platon.browser.redis.dto.NetworkStatRedis;
import com.platon.browser.req.staking.AliveStakingListReq;
import com.platon.browser.req.staking.HistoryStakingListReq;

import javax.validation.Valid;

@Service
public class StakingServiceImpl implements StakingService {

	@Autowired
	private StatisticCacheService statisticCacheService;
	
	@Autowired
	private StakingMapper stakingMapper;
	
	@Autowired
	private CustomStakingMapper customStakingMapper;

	@Autowired
	private I18nUtil i18n;
	
	@Override
	public StakingStatisticNewResp stakingStatisticNew() {
		NetworkStatRedis networkStatRedis = statisticCacheService.getNetworkStatCache();
		StakingStatisticNewResp stakingStatisticNewResp = new StakingStatisticNewResp();
		if(networkStatRedis != null) {
			stakingStatisticNewResp.setAddIssueBegin(networkStatRedis.getAddIssueBegin());
			stakingStatisticNewResp.setAddIssueEnd(networkStatRedis.getAddIssueEnd());
			stakingStatisticNewResp.setBlockReward(networkStatRedis.getBlockReward());
			stakingStatisticNewResp.setCurrentNumber(networkStatRedis.getCurrentNumber());
			stakingStatisticNewResp.setIssueValue(networkStatRedis.getIssueValue());
			stakingStatisticNewResp.setNextSetting(networkStatRedis.getNextSetting());
			stakingStatisticNewResp.setStakingDelegationValue(networkStatRedis.getStakingDelegationValue());
			stakingStatisticNewResp.setStakingReward(networkStatRedis.getStakingReward());
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
			aliveStakingListResp.setDelegateValue(new BigDecimal(stakings.get(i).getStatDelegateHas())
					.add(new BigDecimal(stakings.get(i).getStatDelegateLocked())).toString());
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
			aliveStakingListResp.setTotalValue(new BigDecimal(stakings.get(i).getStakingHas()).add(new BigDecimal(stakings.get(i).getStakingLocked()))
					.add(new BigDecimal(stakings.get(i).getStatDelegateHas())).add(new BigDecimal(stakings.get(i).getStatDelegateLocked())).toString());
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
	public BaseResp<StakingDetailsResp> stakingDetails(@Valid StakingDetailsReq req) {
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
				resp.setTotalValue(new BigDecimal(stakingNode.getStakingHas()).add(new BigDecimal(stakingNode.getStakingLocked()))
						.add(new BigDecimal(stakingNode.getStatDelegateHas())).add(new BigDecimal(stakingNode.getStatDelegateLocked())).toString());
				//委托总金额数=委托交易总金额(犹豫期金额)+委托交易总金额(锁定期金额)
				resp.setDelegateValue(new BigDecimal(stakingNode.getStatDelegateHas())
						.add(new BigDecimal(stakingNode.getStatDelegateLocked())).toString());
				resp.setStakingValue(new BigDecimal(stakingNode.getStakingHas()).add(new BigDecimal(stakingNode.getStakingLocked())).toString());
				resp.setDelegateQty(Integer.toString(stakingNode.getStatDelegateQty()));
				resp.setSlashLowQty(stakingNode.getStatSlashLowQty());
				resp.setSlashMultiQty(stakingNode.getStatSlashMultiQty());
				resp.setBlockQty(stakingNode.getStatBlockQty().intValue());
				resp.setExpectBlockQty(Long.toString(stakingNode.getStatExpectBlockQty()));
				resp.setExpectedIncome(stakingNode.getExpectedIncome());
				resp.setJoinTime(sdf.format(stakingNode.getJoinTime()));
				resp.setVerifierTime(Integer.toString(stakingNode.getStatVerifierTime()));
				resp.setRewardValue(stakingNode.getStatRewardValue());
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

}
