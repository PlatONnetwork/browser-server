package com.platon.browser.controller;

import javax.validation.Valid;

import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * 	 验证人模块接口申明集成swagger
 *  @file AppDocStaking.java
 *  @description 
 *	@author zhangrj
 *  @data 2019年8月31日
 */
@Api(value = "/staking", tags = "Staking")
public interface AppDocStaking {
	
	
    /**
     * @api {subscribe} /topic/staking/statistic/new a.汇总数据（websocket）
     * @apiVersion 1.0.0
     * @apiName topic/staking/statistic/new
     * @apiGroup staking
     * @apiDescription 
     * 1. 功能：推送汇总数据<br/>
     * 2. 实现逻辑：<br/>
     * - 查询redis结构：browser:[应用版本]:[应用运行配置名称]:networkstat<br/>
     * - 5s全量推送一次
     * @apiSuccessExample  Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":{
     *       "stakingDelegationValue":"", //质押委托总数
     *       "stakingValue":"",        //质押总数
     *       "issueValue":"",          //发行量
     *       "blockReward":"",         //当前的出块奖励
     *       "stakingReward":"",       //当前的质押奖励
     *       "currentNumber":111,      //当前区块高度
     *       "addIssueBegin":111,      //当前增发周期的开始快高
     *       "addIssueEnd":111,        //当前增发周期的结束块高
     *       "nextSetting":111         //离下个结算周期倒计时
     *    }
     * }
     */	
	@ApiOperation(value = "topic/staking/statistic/new", nickname = "", notes = "", tags = { "Staking" })
	@SubscribeMapping(value = "topic/staking/statistic/new")
	public BaseResp<StakingStatisticNewResp> stakingStatisticNew();
	
    /**
     * @api {post} /staking/aliveStakingList b.实时验证人列表
     * @apiVersion 1.0.0
     * @apiName aliveStakingList
     * @apiGroup staking
     * @apiDescription 
     * 1. 功能：实时验证人列表查询<br/>
     * 2. 实现逻辑：<br/>
     * - 查询mysql中node表并关联staking表，进程中排序
     * @apiParamExample {json} Request-Example:
     * {
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
     *    "key":""                     //验证人名称
     *    "queryStatus":""             //查询状态
     *                                 all：全部的
     *                                 active：活跃中的
     *                                 candidate：候选中的
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *   "errMsg":"",                  //描述信息
     *   "code":0,                     //成功（0），失败则由相关失败码
     *   "totalCount":18,              //总数
     *   "totalPages":1,               //总页数
     *   "data":[
     *      {
     *         "ranking":11,           //排行
     *         "nodeId":"",            //节点id
     *         "nodeName":"",          //验证人名称
     *         "stakingIcon":"",       //验证人图标
     *         "status":"",            //状态   1:候选中  2:活跃中  3:出块中
     *         "totalValue":"",        //质押总数=有效的质押+委托
     *         "delegateValue":"",     //委托总金额数=委托交易总金额(犹豫期金额)+委托交易总金额(锁定期金额)
     *         "delegateQty":"",       //委托人数（地址数）
     *         "slashLowQty":11,       //低出块率举报次数
     *         "slashMultiQty":11,     //多签举报次数
     *         "blockQty":11,          //产生的区块数
     *         "expectedIncome":"",    //预计年收化率（从验证人加入时刻开始计算）
     *         "isRecommend":true,     //是否官方推荐
     *         "isInit":true           //是否为初始节点 
     *      }
     *   ]
     * }
     */	
	@ApiOperation(value = "staking/aliveStakingList", nickname = "", notes = "", response = AliveStakingListResp.class, tags = { "Staking" })
	@RequestMapping(value = "staking/aliveStakingList", produces = { "application/json" }, method = RequestMethod.POST)
    public RespPage<AliveStakingListResp> aliveStakingList(@ApiParam(value = "AliveStakingListReq ", required = true)@Valid @RequestBody AliveStakingListReq req);
	
	
    /**
     * @api {post} /staking/historyStakingList c.历史验证人列表
     * @apiVersion 1.0.0
     * @apiName historyStakingList
     * @apiGroup staking
     * @apiDescription
     * 1. 功能：历史验证人列表查询<br/>
     * 2. 实现逻辑：<br/>
     * - 查询mysql中node表并关联staking表
     * @apiParamExample {json} Request-Example:
     * {
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
     *    "key":""                     //验证人名称
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *   "errMsg":"",                  //描述信息
     *   "code":0,                     //成功（0），失败则由相关失败码
     *   "totalCount":18,              //总数
     *   "totalPages":1,               //总页数
     *   "data":[
     *      {
     *         "nodeId":"",            //出块节点地址
     *         "nodeName":"",          //验证人名称
     *         "stakingIcon":"",       //验证人图标
     *         "status":"",            //状态 4:退出中 5:已退出
     *         "statDelegateReduction":"", //待提取的委托
     *         "slashLowQty":11,       //低出块率举报次数
     *         "slashMultiQty":11,     //多签举报次数
     *         "leaveTime":11          //退出时间
     *         "blockQty":11,          //产生的区块数
     *      }
     *   ]
     * }
     */	
	@ApiOperation(value = "staking/historyStakingList", nickname = "", notes = "", response = HistoryStakingListResp.class, tags = { "Staking" })
	@RequestMapping(value = "staking/historyStakingList", produces = { "application/json" }, method = RequestMethod.POST)
    public RespPage<HistoryStakingListResp> historyStakingList(@ApiParam(value = "HistoryStakingListReq ", required = true)@Valid @RequestBody HistoryStakingListReq req);
	
	
    /**
     * @api {subscribe} /topic/staking/change/new d.实时验证人变更（websocket）
     * @apiVersion 1.0.0
     * @apiName topic/staking/change/new
     * @apiGroup staking
     * @apiDescription
     * 1. 功能：.实时验证人变更<br/>
     * 2. 实现逻辑：<br/>
     * - TODO 实现方式？
     * @apiSuccessExample  Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":{
     *       "isFlash":"",             //是否需要刷新列表
     *       "其他属性"                   //同实时验证人列表中每个对象属性 
     *    }
     * }
     */	
	@ApiOperation(value = "topic/staking/change/new", nickname = "", notes = "", response = StakingChangeNewResp.class, tags = { "Staking" })
	@SubscribeMapping(value = "topic/staking/change/new")
	public BaseResp<StakingChangeNewResp> stakingChangeNew();
	
    /**
     * @api {post} /staking/stakingDetails e.验证人详情
     * @apiVersion 1.0.0
     * @apiName stakingDetails
     * @apiGroup staking
     * @apiDescription
     * 1. 功能：验证人详情查询<br/>
     * 2. 实现逻辑：<br/>
     * - 查询mysql中node表并关联staking表
     * @apiParamExample {json} Request-Example:
     * {
     *    "nodeId":""                //节点地址
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code": 0,                   //成功（0），失败则由相关失败码
     *    "data": {
     *       "nodeName":"",            //验证人名称
     *       "stakingIcon":"",         //验证人图标
     *       "status":"",              //状态   1:候选中  2:活跃中  3:出块中
     *       "totalValue":"",          //质押总数=有效的质押+委托
     *       "delegateValue":"",       //委托总数
     *       "stakingValue":"",        //质押总数
     *       "delegateQty":"",         //委托人数
     *       "slashLowQty":11,         //低出块率举报次数
     *       "slashMultiQty":11,       //多签举报次数
     *       "blockQty":11,            //产生的区块数
     *       "expectBlockQty":"",      //预计的出块数
     *       "expectedIncome":"",      //预计年收化率（从验证人加入时刻开始计算）
     *       "joinTime":"",            //加入时间
     *       "verifierTime":"",        //进入共识验证轮次数
     *       "rewardValue":"",         //累计的收益 
     *       "nodeId":"",              //节点id
     *       "stakingAddr":"",         //发起质押的账户地址
     *       "denefitAddr":"",         //收益地址
     *       "website":"",             //节点的第三方主页
     *       "details":"",             //节点的描述
     *       "externalId":"",          //身份证id
     *       "externalUrl":"",          //身份证url
     *       "stakingBlockNum":"",     //最新的质押交易块高
     *       "statDelegateReduction":"",//待提取的委托
     *       "isInit":true           //是否为初始节点 
     *    }
     * }
     */	
	@ApiOperation(value = "staking/stakingDetails", nickname = "", notes = "", response = StakingDetailsResp.class, tags = { "Staking" })
	@RequestMapping(value = "staking/stakingDetails", produces = { "application/json" }, method = RequestMethod.POST)
    public BaseResp<StakingDetailsResp> stakingDetails(@ApiParam(value = "StakingDetailsReq ", required = true)@Valid @RequestBody StakingDetailsReq req);
	
    /**
     * @api {post} /staking/stakingOptRecordList f.节点操作记录
     * @apiVersion 1.0.0
     * @apiName stakingOptRecordList
     * @apiGroup staking
     * @apiDescription
     * 1. 功能：节点操作记录查询<br/>
     * 2. 实现逻辑：<br/>
     * - 查询mysql中node_opt表
     * @apiParamExample {json} Request-Example:
     * {
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
     *    "nodeId":""                //节点地址
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *   "errMsg":"",                  //描述信息
     *   "code":0,                     //成功（0），失败则由相关失败码
     *   "totalCount":18,              //总数
     *   "totalPages":1,               //总页数
     *   "data":[
     *      {
     *         "timestamp":"",         //创建时间
     *         "desc":"",              //操作描述（code表示）\r\n1 create 创建 \r\n2 modify 修改\r\n3 quit 退出\r\n4 proposals 提案\r\n5 vote 投票\r\n6 signatures 双签\r\n7 lowBlockRate 出块率低 
     *         "txHash":"",            //所属交易
     *         "blockNumber":11        //所属区块
     *      }
     *   ]
     * }
     */	
	@ApiOperation(value = "staking/stakingOptRecordList", nickname = "", notes = "", response = StakingOptRecordListResp.class, tags = { "Staking" })
	@RequestMapping(value = "staking/stakingOptRecordList", produces = { "application/json" }, method = RequestMethod.POST)
    public RespPage<StakingOptRecordListResp> stakingOptRecordList(@ApiParam(value = "StakingOptRecordListReq ", required = true)@Valid @RequestBody StakingOptRecordListReq req);
	
    /**
     * @api {post} /staking/delegationListByStaking g.验证人相关的委托列表
     * @apiVersion 1.0.0
     * @apiName delegationListByStaking
     * @apiGroup staking
     * @apiDescription
     * 1. 功能：验证人相关的委托列表查询<br/>
     * 2. 实现逻辑：<br/>
     * - 查询mysql中delegation表关联staking，查询 delegation.is_history = false的记录
     * - staking 与 delegation 一对多，delegation的字段需要叠加
     * @apiParamExample {json} Request-Example:
     * {
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
     *    "nodeId":"",               //节点地址
     *    "stakingBlockNum",""         //最新的质押交易块高
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *   "errMsg":"",                  //描述信息
     *   "code":0,                     //成功（0），失败则由相关失败码
     *   "totalCount":18,              //总数
     *   "totalPages":1,               //总页数
     *   "data":[
     *      {
     *         "delegateAddr":"",      //委托人地址    delegation
     *         "delegateValue":"",     //委托金额       delegation   delegate_has+delegate_locked
     *         "delegateTotalValue":"",//验证人委托的总金额    staking    delegate_has+delegate_locked
     *         "delegateLocked":"",    //已锁定委托（LAT）如果关联的验证人状态正常则正常显示，如果其他情况则为零（delegation）
     *         "allDelegateLocked":"", //当前验证人总接收的锁定委托量（LAT）  staking  stat_delegate_locked
     *      }
     *   ]
     * }
     */
	@ApiOperation(value = "staking/delegationListByStaking", nickname = "", notes = "", response = DelegationListByStakingResp.class, tags = { "Staking" })
	@RequestMapping(value = "staking/delegationListByStaking", produces = { "application/json" }, method = RequestMethod.POST)
    public RespPage<DelegationListByStakingResp> delegationListByStaking(@ApiParam(value = "DelegationListByStakingReq ", required = true)@Valid @RequestBody DelegationListByStakingReq req);
	
	
    /**
     * @api {post} /staking/delegationListByAddress h.地址相关的委托列表
     * @apiVersion 1.0.0
     * @apiName delegationListByAddress
     * @apiGroup staking
     * @apiDescription
     * 1. 功能：地址相关的委托列表查询<br/>
     * 2. 实现逻辑：<br/>
     * - 查询mysql中delegation表关联staking，查询 delegation.is_history = false的记录
     * @apiParamExample {json} Request-Example:
     * {
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
     *    "address":500                //委托地址(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *   "errMsg":"",                  //描述信息
     *   "code":0,                     //成功（0），失败则由相关失败码
     *   "totalCount":18,              //总数
     *   "totalPages":1,               //总页数
     *   "data":[
     *      {
     *         "nodeId":"",            //节点id
     *         "nodeName":"",          //节点名称
     *         "delegateValue":"",     //委托数量 delegation   delegateHas+delegateLocked
     *         "delegateHas":"",       //未锁定委托（LAT） 如果关联的验证人状态正常则正常显示，如果其他情况则为零 （delegation）
     *         "delegateLocked":"",    //已锁定委托（LAT）如果关联的验证人状态正常则正常显示，如果其他情况则为零（delegation）
     *         "allDelegateLocked":"", //当前验证人总接收的锁定委托量（LAT）  staking
     *         "delegateUnlock":"",    //已解除委托（LAT）  如果关联的验证人状态退出中或已退出则为delegateHas+delegateLocked，如果其他情况则为0（delegation）
     *         "delegateReduction":""  //赎回中委托（LAT） delegation
     *      }
     *   ]
     * }
     */
	@ApiOperation(value = "staking/delegationListByAddress", nickname = "", notes = "", response = DelegationListByAddressResp.class, tags = { "Staking" })
	@RequestMapping(value = "staking/delegationListByAddress", produces = { "application/json" }, method = RequestMethod.POST)
    public RespPage<DelegationListByAddressResp> delegationListByAddress(@ApiParam(value = "DelegationListByAddressReq ", required = true)@Valid @RequestBody DelegationListByAddressReq req);
	
	
    /**
     * 多余的接口
     * @apiDeprecated
     * @api {get} /staking/delegationListByAddressDownload?address=:address&date=:date i.导出地址验证人委托列表
     * @apiVersion 1.0.0
     * @apiName delegationListByAddressDownload
     * @apiGroup staking
     * @apiDescription 逻辑同 《地址相关的委托列表接口》
     * @apiParam {String} address 合约地址
     * @apiParam {String} date 数据结束日期
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * >响应为 二进制文件流
     * 没有导出
     */
	
	
}
