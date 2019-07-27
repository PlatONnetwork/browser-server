package com.platon.browser.controller;

public class AppDocStaking {
	
	
    /**
     * @api {subscribe} /topic/staking/statistic/new a.汇总数据（websocket）
     * @apiVersion 1.0.0
     * @apiName topic/staking/statistic/new
     * @apiGroup staking
     * @apiDescription 
     * @apiSuccessExample  Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":{
     *       "totalVolume":"",         //质押委托总数
     *       "totalStakingVolume":"",  //质押总数
     *       "issueVolume":"",         //发行量
     *       "curBlockReward":"",      //当前的出块奖励
     *       "curStakingReward":"",    //当前的质押奖励
     *       "currentHeight":111,      //当前区块高度
     *       "addIssueBegin":111,      //当前增发周期的开始快高
     *       "addIssueEnd":111,        //当前增发周期的结束块高
     *       "nextSetting":111         //离下个结算周期倒计时
     *    }
     * }
     */	
	
	
    /**
     * @api {post} /staking/aliveStakingList b.实时验证人列表
     * @apiVersion 1.0.0
     * @apiName aliveStakingList
     * @apiGroup staking
     * @apiDescription 验证人列表
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
     *         "nodeAddr":"",          //出块节点地址
     *         "nodeName":"",          //验证人名称
     *         "stakingIcon":"",       //验证人图标
     *         "status":"",            //状态   1:候选中  2:活跃中  3:出块中
     *         "totalValue":"",        //质押总数=有效的质押+委托
     *         "delegateValue":"",     //委托总数
     *         "delegateQty":"",       //委托人数
     *         "slashLowQty":11,       //低出块率举报次数
     *         "slashMultiQty":11,     //多签举报次数
     *         "blockQty":11,          //产生的区块数
     *         "expectedIncome":""     //预计年收化率（从验证人加入时刻开始计算）
     *         "isRecommend":true      //是否官方推荐 
     *      }
     *   ]
     * }
     */	
	
	
    /**
     * @api {post} /staking/historyStakingList c.历史验证人列表
     * @apiVersion 1.0.0
     * @apiName historyStakingList
     * @apiGroup staking
     * @apiDescription 历史验证人列表
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
     *         "nodeAddr":"",          //出块节点地址
     *         "nodeName":"",          //验证人名称
     *         "stakingIcon":"",       //验证人图标
     *         "status":"",            //状态 4:退出中 5:已退出
     *         "statDelegateReduction":"", //待提取的委托
     *         "reportLow":11,         //低出块率举报次数
     *         "slashLowQty":11,       //低出块率举报次数
     *         "slashMultiQty":11,     //多签举报次数
     *         "leaveTime":11          //退出时间
     *      }
     *   ]
     * }
     */	
	
	
    /**
     * @api {subscribe} /topic/staking/change/new d.实时验证人变更（websocket）
     * @apiVersion 1.0.0
     * @apiName topic/staking/change/new
     * @apiGroup staking
     * @apiDescription 全量数据
     * @apiSuccessExample  Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":{
     *       "isFlash":"",             //是否需要刷新列表
     *       "ranking":11,             //排行
     *       "nodeAddr":"",            //出块节点地址
     *       "nodeName":"",            //验证人名称
     *       "stakingIcon":"",         //验证人图标
     *       "status":"",              //状态   1:候选中  2:活跃中  3:出块中
     *       "totalValue":"",          //质押总数=有效的质押+委托
     *       "delegateValue":"",       //委托总数
     *       "delegateQty":"",         //委托人数
     *       "slashLowQty":11,         //低出块率举报次数
     *       "slashMultiQty":11,       //多签举报次数
     *       "blockQty":11,            //产生的区块数
     *       "expectedIncome":""       //预计年收化率（从验证人加入时刻开始计算）
     *    }
     * }
     */	
	
	
    /**
     * @api {post} /staking/stakingDetails e.验证人详情
     * @apiVersion 1.0.0
     * @apiName stakingDetails
     * @apiGroup staking
     * @apiDescription 验证人详情
     * @apiParamExample {json} Request-Example:
     * {
     *    "nodeAddr":""                //节点地址
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code": 0,                   //成功（0），失败则由相关失败码
     *    "data": {
     *       "ranking":11,             //排行
     *       "nodeAddr":"",            //出块节点地址
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
     *       "stakingBlockNum":"",     //最新的质押交易块高
     *       "statDelegateReduction":""//待提取的委托
     *    }
     * }
     */	
	
	
    /**
     * @api {post} /staking/stakingOptRecordList f.节点操作记录
     * @apiVersion 1.0.0
     * @apiName stakingOptRecordList
     * @apiGroup staking
     * @apiDescription 验证人列表
     * @apiParamExample {json} Request-Example:
     * {
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
     *    "nodeAddr":""                //节点地址
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
     *         "desc":"",              //操作描述
     *         "txHash":"",            //所属交易
     *         "blockNumber":11        //所属区块
     *         
     *      }
     *   ]
     * }
     */	
	
    /**
     * @api {post} /staking/delegationListByStaking g.验证人相关的委托列表
     * @apiVersion 1.0.0
     * @apiName delegationListByStaking
     * @apiGroup staking
     * @apiDescription 验证人相关的委托列表
     * @apiParamExample {json} Request-Example:
     * {
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
     *    "nodeAddr":"",               //节点地址
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
     *         "delegateAddr":"",      //委托人地址
     *         "delegateValue":"",     //委托金额
     *         "delegateTotalValue":"",//验证人委托的总金额
     *         "locketValue":"",       //锁定的委托金额
     *         "locketTotalValue:""    //验证人锁定的委托总额
     *      }
     *   ]
     * }
     */
	
	
	
    /**
     * @api {post} /staking/delegationListByAddress h.地址相关的委托列表
     * @apiVersion 1.0.0
     * @apiName delegationListByAddress
     * @apiGroup staking
     * @apiDescription 地址相关的委托列表
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
     *         "nodeAddr":"",          //节点地址
     *         "nodeName":"",          //节点名称
     *         "delegateValue":"",     //委托数量
     *         "delegateHes":"",       //未锁定委托（LAT）
     *         "delegateLocked":"",    //已锁定委托（LAT）
     *         "allDelegateLocked":"", //当前验证人总接收的锁定委托量（LAT）
     *         "delegateUnlock":"",    //已解除委托（LAT） 
     *         "delegateReduction":""  //赎回中委托（LAT） 
     *      }
     *   ]
     * }
     */
	
	
	
    /**
     * @api {get} /staking/delegationListByAddressDownload?address=:address&date=:date i.导出地址验证人委托列表
     * @apiVersion 1.0.0
     * @apiName delegationListByAddressDownload
     * @apiGroup staking
     * @apiDescription 导出地址验证人委托列表
     * @apiParam {String} address 合约地址
     * @apiParam {String} date 数据结束日期
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * >响应为 二进制文件流
     */
	
	
}
