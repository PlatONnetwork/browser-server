package com.platon.browser.now.service;

import com.platon.browser.req.staking.AliveStakingListReq;
import com.platon.browser.req.staking.DelegationListByAddressReq;
import com.platon.browser.req.staking.DelegationListByStakingReq;
import com.platon.browser.req.staking.HistoryStakingListReq;
import com.platon.browser.req.staking.StakingDetailsReq;
import com.platon.browser.req.staking.StakingOptRecordListReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.RespPage;
import com.platon.browser.res.staking.*;

/**
 * 质押模块方法接口实现
 *  @file StakingService.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
public interface StakingService {
	
	/**
	 * 质押统计websocket推送
	 * @method stakingStatisticNew
	 * @return
	 */
	 StakingStatisticNewResp stakingStatisticNew();
	
	/**
	 * 实时验证人列表分页查询
	 * @method aliveStakingList
	 * @param req
	 * @return
	 */
	 RespPage<AliveStakingListResp> aliveStakingList(AliveStakingListReq req);
	
	/**
	 * 历史验证人列表分页查询
	 * @method historyStakingList
	 * @param req
	 * @return
	 */
	 RespPage<HistoryStakingListResp> historyStakingList(HistoryStakingListReq req);

	/**
	 * 根绝信息查询单个质押人详情
	 * @method stakingDetails
	 * @param req
	 * @return
	 */
	BaseResp<StakingDetailsResp> stakingDetails( StakingDetailsReq req);

	/**
	 * 根据节点id查询验证人操作信息
	 * @method stakingOptRecordList
	 * @param req
	 * @return
	 */
	 RespPage<StakingOptRecordListResp> stakingOptRecordList( StakingOptRecordListReq req);

	/**
	 * 根据请求查询验证人委托信息
	 * @method delegationListByStaking
	 * @param req
	 * @return
	 */
	 RespPage<DelegationListByStakingResp> delegationListByStaking( DelegationListByStakingReq req);

	/**
	 * 根据地址查询委托信息
	 * @method delegationListByAddress
	 * @param req
	 * @return
	 */
	 RespPage<DelegationListByAddressResp> delegationListByAddress( DelegationListByAddressReq req);
	
}
