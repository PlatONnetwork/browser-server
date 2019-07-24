package com.platon.browser.controller;

public class AppDocTransaction {
	
    /**
     * @api {post} /transaction/transactionList a.交易列表
     * @apiVersion 1.0.0
     * @apiName transactionList
     * @apiGroup transaction
     * @apiDescription
     * 1. 功能：交易列表查询<br/>
     * 2. 实现逻辑：<br/>
     * - 查询redis结构：browser:[应用版本]:[应用运行配置名称]:chain[链ID]:transactions<br/>
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *   "errMsg":"",                  //描述信息
     *   "code":0,                     //成功（0），失败则由相关失败码
     *   "displayTotalCount":18,       //显示总数
     *   "totalCount":18,              //总数
     *   "totalPages":1,               //总页数
     *   "data":[
     *      {
     *         "txHash":"0x234234",    //交易hash
     *         "from":"0x667766",      //发送方地址
     *         "to":"0x667766",        //接收方地址（操作地址）
     *         "value":"222",          //金额(单位:Energon)
     *         "actualTxCost":"22",    //交易费用(单位:Energon)
     *         "txType":""             //交易类型
     *                                 0：转账  1：合约发布  2：合约调用    5：MPC交易
     *                                 1000: 发起质押  1001: 修改质押信息  1002: 增持质押  1003: 撤销质押 1004: 发起委托  1005: 减持/撤销委托
     *                                 2000: 提交文本提案 2001: 提交升级提案 2002: 提交参数提案 2003: 给提案投票 2004: 版本声明
     *                                 3000: 举报多签
     *                                 4000: 创建锁仓计划
     *         "serverTime"1123123,    //服务器时间
     *         "timestamp":18080899999,//交易时间
     *         "blockHeight":"15566",  //交易所在区块高度
     *         "failReason":"",        //失败原因
     *         "receiveType":"account" //此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址，
     *                                 //前端页面在点击接收方的地址时，根据此字段来决定是跳转到账户详情还是合约详情
     *      }
     *   ]
     * }
     */
	
	
    /**
     * @api {post} /transaction/transactionListByBlock b.区块的交易列表
     * @apiVersion 1.0.0
     * @apiName transactionListByBlock
     * @apiGroup transaction
     * @apiDescription
     * 1. 功能：区块的交易列表查询<br/>
     * 2. 实现逻辑：<br/>
     * - 查询mysql中transaction表
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
     *    "blockNumber":500,           //区块号(必填)
     *    "txType":"",                 //交易类型 (可选), 如不不传代表全部。
     *                                 transfer：交易
     *                                 delegate：委托相关交易
     *                                 staking：验证人相关交易
     *                                 proposal：治理相关交易
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
     *         "txHash":"0x234234",    //交易hash
     *         "from":"0x667766",      //发送方地址
     *         "to":"0x667766",        //接收方地址（操作地址）
     *         "value":"222",          //金额(单位:Energon)
     *         "actualTxCost":"22",    //交易费用(单位:Energon)
     *         "txType":"",            //交易类型
     *                                 0：转账  1：合约发布  2：合约调用    5：MPC交易
     *                                 1000: 发起质押  1001: 修改质押信息  1002: 增持质押  1003: 撤销质押 1004: 发起委托  1005: 减持/撤销委托
     *                                 2000: 提交文本提案 2001: 提交升级提案 2002: 提交参数提案 2003: 给提案投票 2004: 版本声明
     *                                 3000: 举报多签
     *                                 4000: 创建锁仓计划
     *         "failReason":"",        //失败原因
     *         "receiveType":"account" //此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址，
     *                                 //前端页面在点击接收方的地址时，根据此字段来决定是跳转到账户详情还是合约详情
     *      }
     *   ]
     * }
     */
	
	
    /**
     * @api {post} /transaction/transactionListByAddress c.地址的交易列表
     * @apiVersion 1.0.0
     * @apiName transactionListByAddress
     * @apiGroup transaction
     * @apiDescription
     * 1. 功能：地址的交易列表查询<br/>
     * 2. 实现逻辑：<br/>
     * - 查询mysql中transaction表
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
     *    "address":"0x",              //地址(必填)
     *    "txType":""                  //交易类型 (可选), 如不不传代表全部。
     *                                 transfer：基础交易
     *                                 delegate：委托相关交易
     *                                 staking：验证人相关交易
     *                                 proposal：治理相关交易
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *   "errMsg":"",                  //描述信息
     *   "code":0,                     //成功（0），失败则由相关失败码
     *   "totalCount":18,              //总数
     *   "data":[
     *      {
     *         "txHash":"0x234234",    //交易hash
     *         "from":"0x667766",      //发送方地址
     *         "to":"0x667766",        //接收方地址（操作地址）
     *         "value":"222",          //金额(单位:Energon)
     *         "actualTxCost":"22",    //交易费用(单位:Energon)
     *         "txType":"",            //交易类型
     *                                 0：转账  1：合约发布  2：合约调用    5：MPC交易
     *                                 1000: 发起质押  1001: 修改质押信息  1002: 增持质押  1003: 撤销质押 1004: 发起委托  1005: 减持/撤销委托
     *                                 2000: 提交文本提案 2001: 提交升级提案 2002: 提交参数提案 2003: 给提案投票 2004: 版本声明
     *                                 3000: 举报多签
     *                                 4000: 创建锁仓计划
     *         "failReason":"",        //失败原因
     *         "receiveType":"account",//此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址，
     *                                 //前端页面在点击接收方的地址时，根据此字段来决定是跳转到账户详情还是合约详情
     *         "timestamp":18080899999,//交易时间
     *         "blockHeight":"15566"   //交易所在区块高度
     *      }
     *   ]
     * }
     */
	
	
    /**
     * @api {get} transaction/addressTransactionDownload?cid=:cid&address=:address&date=:date d.导出地址交易列表
     * @apiVersion 1.0.0
     * @apiName addressTransactionDownload
     * @apiGroup transaction
     * @apiDescription
     * 1. 功能：导出地址的交易列表查询<br/>
     * 2. 实现逻辑：<br/>
     * - 查询mysql中transaction表
     * @apiParam {String} cid 链ID
     * @apiParam {String} address 合约地址
     * @apiParam {String} date 数据结束日期
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * >响应为 二进制文件流
     */
	
	
    /**
     * @api {post} transaction/transactionDetails e.交易详情  TODO
     * @apiVersion 1.0.0
     * @apiName transactionDetails
     * @apiGroup transaction
     * @apiDescription 交易详情
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
     *    "txHash":"",                 //交易Hash(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":{
     *       "txHash":"0x234234",      //交易hash
     *       "from":"0x667766",        //发送方地址
     *       "to":"0x667766",          //接收方地址（操作地址）
     *       "timestamp":123123879,    //交易时间
     *       "serverTime"1123123,      //服务器时间
     *       "confirmNum":444,         //区块确认数
     *       "blockHeight":"15566",    //交易所在区块高度
     *       "energonLimit":232,       //能量限制
     *       "energonUsed":122,        //能量消耗
     *       "energonPrice":122,       //能量价格
     *       "value":"222",            //金额(单位:Energon)
     *       "actualTxCost":"22",      //交易费用(单位:Energon)
     *       "txType":"",              //交易类型
     *                                 0：转账  1：合约发布  2：合约调用    5：MPC交易
     *                                 1000: 发起质押  1001: 修改质押信息  1002: 增持质押  1003: 撤销质押 1004: 发起委托  1005: 减持/撤销委托
     *                                 2000: 提交文本提案 2001: 提交升级提案 2002: 提交参数提案 2003: 给提案投票 2004: 版本声明
     *                                 3000: 举报多签
     *                                 4000: 创建锁仓计划
     *       "inputData":"",           //附加输入数据
     *       "txInfo":"{jsonObject}"   //附加输入数据解析后的结构
     *       "failReason":"",          //失败原因
     *       "first":false,            //是否第一条记录
     *       "last":true,              //是否最后一条记录
     *       "receiveType":"account",  //此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址，
     *                                 //前端页面在点击接收方的地址时，根据此字段来决定是跳转到账户详情还是合约详情
     *       "nodeAddr":"",            //被质押的节点地址  当 txType = 1000 或 1001 或 1002 存在
     *       "stakingName":"",         //验证人名称    当 txType = 1002 或 1003 存在,使用nodeId从staking表中查询当前的验证人
     *       "stakingHash":"",         //验证人id  当 txType = 1000 存在,stakingHash = txHash
     *                                           当 txType = 1001 或 1002 或 1003 存在,使用nodeId从staking表中查询当前的验证人
     *       "stakingValue":"",        //质押的总金额（可以退回的总额）：当 txType = 1003 存在
     *       "stakingLocked":"",       //质押锁定的金额（退回锁定的金额）：当 txType = 1003 存在
     *       "stakingRefundStatus":"", //质押金退回状态： 1：退回中  2：退回成功   当 txType = 1003 存在
     *       "depositStatus":"1",      //退回状态， 1： 退回中   2：退回成功  
     *       "depositLock":""          //剩余退回
     * }
     * 
     * txType = 0 or 1 or 2 or 5 时：txInfo信息：null
     * txType = 1000 时：txInfo信息：
     * {
     *    "type":0,                    //表示使用账户自由金额还是账户的锁仓金额做质押，0: 自由金额； 1: 锁仓金额
     *    "benefitAddress":"",         //用于接受出块奖励和质押奖励的收益账户
     *    "nodeId":"",                 //被质押的节点Id(也叫候选人的节点Id)
     *    "externalId":"",             //外部Id(有长度限制，给第三方拉取节点描述的Id)
     *    "nodeName":"",               //被质押节点的名称(有长度限制，表示该节点的名称)
     *    "website":"",                //节点的第三方主页(有长度限制，表示该节点的主页)
     *    "details":"",                //节点的描述(有长度限制，表示该节点的描述)
     *    "amount":"",                 //质押的von
     *    "programVersion":""          //程序的真实版本，治理rpc获取
     * }
     * txType = 1001 时：txInfo信息：
     * {
     *    "benefitAddress":"",         //用于接受出块奖励和质押奖励的收益账户
     *    "nodeId":"",                 //被质押的节点Id(也叫候选人的节点Id)
     *    "externalId":"",             //外部Id(有长度限制，给第三方拉取节点描述的Id)
     *    "nodeName":"",               //被质押节点的名称(有长度限制，表示该节点的名称)
     *    "website":"",                //节点的第三方主页(有长度限制，表示该节点的主页)
     *    "details":""                 //节点的描述(有长度限制，表示该节点的描述)
     * }
     * txType = 1002 时：txInfo信息：
     * {
     *    "type":0,                    //表示使用账户自由金额还是账户的锁仓金额做质押，0: 自由金额； 1: 锁仓金额
     *    "nodeId":"",                 //被质押的节点Id(也叫候选人的节点Id)
     *    "amount":""                  //质押的von
     * }
     * txType = 1003 时：txInfo信息：
     * {
     *    "nodeId":""                  //被质押的节点Id(也叫候选人的节点Id)
     * }
     * 
     * //TODO
     * 
     * txType = 1004 时：txInfo信息：
     * {
     *    "type":0,                    //表示使用账户自由金额还是账户的锁仓金额做质押，0: 自由金额； 1: 锁仓金额
     *    "nodeId":"",                 //被质押的节点Id(也叫候选人的节点Id)
     *    "amount":""                  //委托的金额(按照最小单位算，1LAT = 10**18 von)
     * }
     * txType = 1005 时：txInfo信息：
     * {
     *    "stakingBlockNum":111,       //代表着某个node的某次质押的唯一标示
     *    "nodeId":"",                 //被质押的节点Id(也叫候选人的节点Id)
     *    "amount":""                  //减持委托的金额(按照最小单位算，1LAT = 10**18 von)
     * }
     * txType = 2000 时：txInfo信息：
     * {
     *    "verifier":111,              //提交提案的验证人
     *    "githubID":"",               //提案在github上的id
     *    "topic":"",                  //提案主题，长度不超过128
     *    "desc":"",                   //提案描述，长度不超过512
     *    "url":"",                    //提案URL，长度不超过512
     *    "endVotingBlock":111         //提案投票截止块高（EpochSize*N-20，不超过2周的块高）
     * }
     * txType = 2001 时：txInfo信息：
     * {
     *    "verifier":111,              //提交提案的验证人
     *    "githubID":"",               //提案在github上的id
     *    "topic":"",                  //提案主题，长度不超过128
     *    "desc":"",                   //提案描述，长度不超过512
     *    "url":"",                    //提案URL，长度不超过512
     *    "endVotingBlock":111,        //提案投票截止块高（EpochSize*N-20，不超过2周的块高）
     *    "newVersion":11,             //升级版本
     *    "activeBlock":111            //（如果投票通过）生效块高（endVotingBlock+ 4*250 ~ endVotingBlock+ 10*250）
     * }
     * txType = 2002 时：txInfo信息：
     * {
     *    "verifier":111,              //提交提案的验证人
     *    "githubID":"",               //提案在github上的id
     *    "topic":"",                  //提案主题，长度不超过128
     *    "desc":"",                   //提案描述，长度不超过512
     *    "url":"",                    //提案URL，长度不超过512
     *    "endVotingBlock":111,        //提案投票截止块高（EpochSize*N-20，不超过2周的块高）
     *    "paramName":"",              //参数名称
     *    "currentValue":"",           //当前值
     *    "newValue":""                //新的值
     * }
     * txType = 2003 时：txInfo信息：
     * {
     *    "verifier":111,              //投票的验证人
     *    "proposalID":"",             //提案ID
     *    "option":""                  //投票选项  0x01：文本提案    0x02：升级提案   0x03：参数提案
     * }
     * txType = 2004 时：txInfo信息：
     * {
     *    "activeNode":111,            //声明的节点，只能是验证人/候选人
     *    "version":111                //声明的版本
     * }
     * txType = 3000 时：txInfo信息：
     * {
     *    "data":"{json}"              //证据的json值，格式为RPC接口Evidences的返回值
     * }
     * txType = 4000 时：txInfo信息：
     * {
     *    "account":""                 //锁仓释放到账账户
     *    "plan":[
     *       {
     *          "epoch":11,            //表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。Epoch * 每周期的区块数至少要大于最高不可逆区块高度
     *          "amount":111           //表示目标区块上待释放的金额
     *       }
     *    ]
     * }
     */	
	
	
    /**
     * @api {post} transaction/transactionDetailNavigate h.交易详情前后跳转浏览
     * @apiVersion 1.0.0
     * @apiName transactionDetailNavigate
     * @apiGroup transaction
     * @apiDescription 交易详情前后跳转浏览
     * @apiParamExample {json} Request-Example:
     * {
     *    "cid":"",                    //链ID (必填)
     *    "txHash":""                  //交易Hash(必填)
     *    "direction":"",              //方向：prev-上一个，next-下一个 (必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * { }                             //参考： transaction/transactionDetails 接口返回
     */
	
	
	
	
}
