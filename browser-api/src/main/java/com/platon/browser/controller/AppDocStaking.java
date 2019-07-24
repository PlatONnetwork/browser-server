package com.platon.browser.controller;

public class AppDocStaking {
	
	
    /**
     * @api {subscribe} /topic/staking/statistic/new?cid=:chainId c.staking基础数据（websocket请求）全量数据
     * @apiVersion 1.0.0
     * @apiName topic/staking/statistic/new
     * @apiGroup staking
     * @apiDescription 全量数据
     * @apiParam {String} cid 链ID.
     * @apiSuccessExample  Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":{
     *       "totalVolume":"",         //质押总数=有效的质押+委托
     *       "issueVolume":"",         //发行量
     *       "delegateVolume":"",      //委托总数
     *       "currentHeight":111,      //当前区块高度
     *       "addFinishBlock":111,     //当前增发周期的结束快高
     *       "addIndexBlock":111,      //当前快高处于增发周期的index
     *       "nextSetting":111         //离下个结算周期倒计时
     *    }
     * }
     */	
	
	
    /**
     * @api {post} staking/aliveStakingList a.委托人相关的验证人列表
     * @apiVersion 1.0.0
     * @apiName aliveStakingList
     * @apiGroup staking
     * @apiDescription 验证人列表
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
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
     *         "nodeAddress":"",       //出块节点地址
     *         "stakingName":"",       //验证人名称
     *         "stakingHash":"",       //验证人id
     *         "ranking":11,           //排行
     *         "status":"",            //状态 1:活跃中 2:候选中 3:出块中
     *         "totalVolume":"",       //质押总数=有效的质押+委托
     *         "delegateVolume":"",    //委托总数
     *         "delegateCount":"",     //委托人数
     *         "reportLow":11,         //低出块率举报次数
     *         "reportMutiSign":11,    //多签举报次数
     *         "generateBlock":11,     //产生的区块数
     *         "expectedIncome":""     //预计年收化率（从验证人加入时刻开始计算）
     *         
     *      }
     *   ]
     * }
     */	
	
	
    /**
     * @api {post} staking/historyStakingList a.委托人相关的验证人列表
     * @apiVersion 1.0.0
     * @apiName historyStakingList
     * @apiGroup staking
     * @apiDescription 验证人列表
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
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
     *         "nodeAddress":"",       //出块节点地址
     *         "stakingName":"",       //验证人名称
     *         "stakingHash":"",       //验证人id
     *         "ranking":11,           //排行
     *         "status":"",            //状态 4:退出中 5:已退出
     *         "reductionVol":"",      //待提取的委托
     *         "reportLow":11,         //低出块率举报次数
     *         "reportMutiSign":11,    //多签举报次数
     *         "generateBlock":11,     //产生的区块数
     *         "leaveTime":11          //退出时间
     *      }
     *   ]
     * }
     */	
	
	
    /**
     * @api {subscribe} /topic/staking/change/new?cid=:chainId c.staking数据变更
     * @apiVersion 1.0.0
     * @apiName topic/staking/statistic/new
     * @apiGroup staking
     * @apiDescription 全量数据
     * @apiParam {String} cid 链ID.
     * @apiSuccessExample  Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":{
     *       "isFlash":"",             //是否需要刷新列表
     *       "nodeAddress":"",         //出块节点地址
     *       "stakingName":"",         //验证人名称
     *       "stakingHash":"",         //验证人id
     *       "ranking":11,             //排行
     *       "status":"",              //状态 1:活跃中 2:候选中 3:出块中
     *       "totalVolume":"",         //质押总数=有效的质押+委托
     *       "delegateVolume":"",      //委托总数
     *       "delegateCount":"",       //委托人数
     *       "reportLow":11,           //低出块率举报次数
     *       "reportMutiSign":11,      //多签举报次数
     *       "generateBlock":11,       //产生的区块数
     *       "expectedIncome":""       //预计年收化率（从验证人加入时刻开始计算）
     *    }
     * }
     */	
	
	
    /**
     * @api {post} staking/stakingOptRecordList a.委托人相关的验证人列表
     * @apiVersion 1.0.0
     * @apiName aliveStakingList
     * @apiGroup staking
     * @apiDescription 验证人列表
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
     *    "stakingHash":"",            //验证人id
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
     *         "createTime":"",        //创建时间
     *         "optDesc":"",           //操作描述
     *         "transactionHash":"",   //所属交易
     *         "blockNumber":11        //所属区块
     *         
     *      }
     *   ]
     * }
     */	
	
	
    /**
     * @api {post} staking/stakingDetails b.验证人详情
     * @apiVersion 1.0.0
     * @apiName stakingDetails
     * @apiGroup staking
     * @apiDescription 验证人详情
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
     *    "stakingHash":"",            //验证人id
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code": 0,                   //成功（0），失败则由相关失败码
     *    "data": {
     *       "stakingName":"",         //验证人名称
     *       "stakingHash":"",         //验证人id
     *       "nodeId":"",              //节点ID
     *       "nodeAddress":"",         //节点地址
     *       "logo":"",                //节点LOGO，url， 如果不存在默认图片
     *       "location":"",            //所属区域
     *       "joinTime":199880011,     //加入时间，单位-毫秒
     *       "status":"",              //状态 1:活跃中 2:候选中 3:出块中 4:退出中 5:已退出
     *       "verifyCount":44554,      //被选为共识轮验证人的累计次数
     *       "blockCount":252125,      //累计出块数
     *       "blockRato":"",           //出块率
     *       "rewardAmount": "0.1",    //累计奖励(单位:Energon)
     *       "expectedIncome":"",      //预计年收化率（从验证人加入时刻开始计算）
     *       "reportLow":11,           //低出块率举报次数
     *       "reportMutiSign":11,      //多签举报次数
     *       "totalVolume":"",         //质押总数
     *       "stakingVolume":"",       //质押总数
     *       "delegateVolume":"",      //委托总数  
     *       "delegateCount":"",       //委托人数
     *       "benefitAddress":"",      //收益的地址
     *       "optAddress":"",          //操作的地址
     *       "website":"",             //节点的第三方主页
     *       "details":"",             //节点的描述
     *       "externalId":""           //身份证id
     *       "stakingRetStatus":"",    //委托退回状态
     *       "delegateRetStatus":"",   //质押退回状态
     *    }
     * }
     */	
	
	
    /**
     * @api {post} staking/stakingListByDelegateAddress a.委托人相关的验证人列表
     * @apiVersion 1.0.0
     * @apiName stakingListByDelegateAddress
     * @apiGroup staking
     * @apiDescription 区块交易列表
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
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
     *         "nodeAddress":"",       //出块节点地址
     *         "stakingName":"",       //验证人名称
     *         "stakingHash":"",       //验证人id
     *         "delegateValue":"",     //委托数量
     *         "delegateHes":"",       //未锁定委托（LAT）
     *         "delegateLocked":"",    //已锁定委托（LAT）
     *         "allDelegateLocked":"", //当前验证人总接收的锁定委托量（LAT）
     *         "delegateReleased":"",  //已解除委托（LAT）   
     *         "delegateReduction":""  //赎回中委托（LAT）   
     *      }
     *   ]
     * }
     */
	
	
	
    /**
     * @api {get} staking/addressStakingDownload?cid=:cid&address=:address&date=:date h.导出地址验证人委托列表
     * @apiVersion 1.0.0
     * @apiName addressStakingDownload
     * @apiGroup staking
     * @apiDescription 导出地址验证人委托列表
     * @apiParam {String} cid 链ID
     * @apiParam {String} address 合约地址
     * @apiParam {String} date 数据结束日期
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * >响应为 二进制文件流
     */
	
	
    /**
     * @api {post} staking/delegationListByStaking a.委托人相关的验证人列表
     * @apiVersion 1.0.0
     * @apiName delegationListByStaking
     * @apiGroup staking
     * @apiDescription 验证人列表
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
     *    "stakingHash":"",            //验证人id
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
     *         "delegateAddress":"",   //委托人地址
     *         "optDesc":"",           //操作描述
     *         "transactionHash":"",   //所属交易
     *         "delegateVolume":"",    //委托数量
     *         "delegateTotalVolume:"",//总委托数量
     *         "locketVolume":"",      //委托数量
     *         "locketTotalVolume:"",   /总委托数量
     *      }
     *   ]
     * }
     */	
	
	
}
