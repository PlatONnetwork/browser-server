package com.platon.browser.controller;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.platon.browser.dto.RespPage;
import com.platon.browser.req.PageReq;
import com.platon.browser.req.newtransaction.TransactionDetailNavigateReq;
import com.platon.browser.req.newtransaction.TransactionDetailsReq;
import com.platon.browser.req.newtransaction.TransactionListByAddressRequest;
import com.platon.browser.req.newtransaction.TransactionListByBlockRequest;
import com.platon.browser.res.BaseResp;
import com.platon.browser.res.transaction.TransactionDetailsResp;
import com.platon.browser.res.transaction.TransactionListResp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api(value = "/transaction", tags = "Transaction")
public interface AppDocTransaction {
	
    /**
     * @api {post} /transaction/transactionList a.交易列表
     * @apiVersion 1.0.0
     * @apiName transactionList
     * @apiGroup transaction
     * @apiDescription
     * 1. 功能：交易列表查询<br/>
     * 2. 实现逻辑：<br/>
     * - 查询redis结构：browser:transactions<br/>
     * @apiParamExample {json} Request-Example:
     * {
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10                //页大小(必填)
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
     *         "from":"0x667766",      //发送方地址（操作地址）
     *         "to":"0x667766",        //接收方地址
     *         "value":"222",          //金额(单位:von)
     *         "actualTxCost":"22",    //交易费用(单位:von)
     *         "txType":""             //交易类型
     *                                 0：转账  1：合约发布  2：合约调用    5：MPC交易
     *                                 1000: 发起质押  1001: 修改质押信息  1002: 增持质押  1003: 撤销质押 1004: 发起委托  1005: 减持/撤销委托
     *                                 2000: 提交文本提案 2001: 提交升级提案 2002: 提交参数提案 2003: 给提案投票 2004: 版本声明
     *                                 3000: 举报多签
     *                                 4000: 创建锁仓计划
     *         "serverTime"1123123,    //服务器时间
     *         "timestamp":18080899999,//出块时间
     *         "blockNumber":"15566",  //交易所在区块高度
     *         "failReason":"",        //失败原因
     *         "receiveType":"account" //此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址，
     *                                 //前端页面在点击接收方的地址时，根据此字段来决定是跳转到账户详情还是合约详情
     *      }
     *   ]
     * }
     */
	@ApiOperation(value = "transaction/transactionList", nickname = "", notes = "", response = TransactionListResp.class, tags = { "Transaction" })
	@RequestMapping(value = "transaction/transactionList", produces = { "application/json" }, method = RequestMethod.POST)
    public RespPage<TransactionListResp> transactionList(@ApiParam(value = "PageReq ", required = true)@Valid @RequestBody PageReq req);
		
	
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
     *    "pageNo":1,                  //页数(必填)
     *    "pageSize":10,               //页大小(必填)
     *    "blockNumber":500,           //区块号(必填)
     *    "txType":""                  //交易类型 (可选), 如不不传代表全部。
     *                                 transfer：交易
     *                                 delegate：委托相关交易
     *                                 staking：验证人相关交易
     *                                 proposal：治理相关交易
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * > 返回值同《交易列表接口》返回值
     */
	@ApiOperation(value = "transaction/transactionListByBlock", nickname = "", notes = "", response = TransactionListResp.class, tags = { "Transaction" })
	@RequestMapping(value = "transaction/transactionListByBlock", produces = { "application/json" }, method = RequestMethod.POST)
    public RespPage<TransactionListResp> transactionListByBlock(@ApiParam(value = "TransactionListByBlockRequest ", required = true)@Valid @RequestBody TransactionListByBlockRequest req);
	
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
     * > 返回值同《交易列表接口》返回值
     */
	@ApiOperation(value = "transaction/transactionListByAddress", nickname = "", notes = "", response = TransactionListResp.class, tags = { "Transaction" })
	@RequestMapping(value = "transaction/transactionListByAddress", produces = { "application/json" }, method = RequestMethod.POST)
    public RespPage<TransactionListResp> transactionListByAddress(@ApiParam(value = "TransactionListByAddressRequest ", required = true)@Valid @RequestBody TransactionListByAddressRequest req);
	
    /**
     * @api {get} /transaction/addressTransactionDownload?address=:address&date=:date d.导出地址交易列表
     * @apiVersion 1.0.0
     * @apiName addressTransactionDownload
     * @apiGroup transaction
     * @apiDescription
     * 1. 功能：导出地址的交易列表查询<br/>
     * 2. 实现逻辑：<br/>
     * - 查询mysql中transaction表
     * @apiParam {String} address 合约地址
     * @apiParam {String} date 数据结束日期
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * >响应为 二进制文件流
     */
	@ApiOperation(value = "transaction/addressTransactionDownload", nickname = "", notes = "", response = TransactionListResp.class, tags = { "Transaction" })
	@RequestMapping(value = "transaction/addressTransactionDownload", produces = { "application/json" }, method = RequestMethod.GET)
    public void addressTransactionDownload(@ApiParam(value = "address ", required = false)@RequestParam(value = "address", required = false)String address,
    		@ApiParam(value = "date ", required = true)@RequestParam(value = "date", required = true)String date);
	
    /**
     * @api {post} transaction/transactionDetails e.交易详情 
     * @apiVersion 1.0.0
     * @apiName transactionDetails
     * @apiGroup transaction
     * @apiDescription
     * 1. 查询mysql中transaction表<br/>
     * - 如果txType = 0（转账）：转账信息从基本信息中取
     * - 如果txType = 1、2、5（合约创建执行）：合约信息从基本信息中取
     * - 如果txType = 1000（创建验证人）： 创建验证人 = nodeId + nodeName + externalId + benefitAddr + programVersion + website + details + value
     * - 如果txType = 1001（编辑验证人）：编辑验证人 = nodeId + nodeName + externalId + benefitAddr + programVersion + website + details
     * - 如果txType = 1002（增加质押）：增加质押 = nodeId + nodeName + value
     * - 如果txType = 1003（退出验证人）：退出验证 = nodeId + nodeName + applyAmount + redeemLocked + redeemStatus + redeemUnLockedBlock
     *   - 1003 需要通过txHash关联staking表查询验证人信息
     * - 如果txType = 1004（委托）：验证人 = nodeId + nodeName
     * - 如果txType = 1005（委托赎回）：委托赎回 = nodeId + nodeName + applyAmount + redeemLocked + redeemStatus
     *   - 1005 需要通过txHash 关联un_delegation表查询赎回的信息
     * - 如果txType = 2000、2001、2002（创建提案）：创建提案 = nodeId + nodeName + txType + proposalUrl + proposalHash + proposalNewVersion
     * - 如果txType = 2003（投票提案）：投票提案 = nodeId + nodeName + txType + proposalUrl + proposalHash + proposalNewVersion +  proposalOption
     * - 如果txType = 2004（版本声明）：版本声明 = nodeId + nodeName + declareVersion
     * - 如果txType = 4000（创建锁仓）：创建锁仓 = RPAccount + value + RPPlan
     * 
     * @apiParamExample {json} Request-Example:
     * {
     *    "txHash":""                  //交易Hash(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *    "errMsg":"",                 //描述信息
     *    "code":0,                    //成功（0），失败则由相关失败码
     *    "data":{
     *       --交易基本信息开始
     *       "txHash":"0x234234",      //交易hash
     *       "from":"0x667766",        //发送方地址
     *       "to":"0x667766",          //接收方地址（操作地址）
     *       "timestamp":123123879,    //交易时间
     *       "serverTime"1123123,      //服务器时间
     *       "confirmNum":444,         //区块确认数
     *       "blockNumber":"15566",    //交易所在区块高度
     *       "gasLimit":232,           //能量限制
     *       "gasUsed":122,            //能量消耗
     *       "gasPrice":122,           //能量价格
     *       "value":"222",            //金额(单位:von)
     *       "actualTxCost":"22",      //交易费用(单位:von)
     *       "txType":"",              //交易类型
     *                                 0：转账  1：合约发布  2：合约调用    5：MPC交易
     *                                 1000: 发起质押  1001: 修改质押信息  1002: 增持质押  1003: 撤销质押 1004: 发起委托  1005: 减持/撤销委托
     *                                 2000: 提交文本提案 2001: 提交升级提案 2002: 提交参数提案 2003: 给提案投票 2004: 版本声明
     *                                 3000: 举报多签
     *                                 4000: 创建锁仓计划
     *       "input":"",               //附加输入数据
     *       "txInfo":"{jsonObject}"   //附加输入数据解析后的结构
     *       "failReason":"",          //失败原因
     *       "first":false,            //是否第一条记录
     *       "last":true,              //是否最后一条记录
     *       "receiveType":"account",  //此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址，
     *                                 //前端页面在点击接收方的地址时，根据此字段来决定是跳转到账户详情还是合约详情
     *        --交易基本信息结束
     *        --可选信息开始
     *       "RPAccount":"",           //锁仓计划的地址
     *       "RPNum":"",				//锁仓数量
     *       "RPPlan":[
     *          {
     *             "epoch":11,         //锁仓周期
     *             "amount":111,       //锁定金额
     *             "blockNumber":11    //锁仓周期对应快高  结束周期 * epoch  
     *          }
     *       ],
     *       "evidences":[             //举报的证据
     *          {
     *             "verify":"",        //节点id
     *             "nodeName":""       //被举报的节点名称
     *          }
     *        ],
     *       "nodeId":"",              //节点id
     *       "nodeName":"",            //节点名称
     *       "benefitAddr":"",         //用于接受出块奖励和质押奖励的收益账户
     *       "externalId":"",          //外部Id(有长度限制，给第三方拉取节点描述的Id)
     *       "website":"",             //节点的第三方主页(有长度限制，表示该节点的主页)
     *       "details":"",             //节点的描述(有长度限制，表示该节点的描述)
     *       "programVersion":"",      //程序的真实版本，治理rpc获取
     *       "applyAmount":"",         //申请赎回的金额
     *       "redeemLocked":"",        //赎回中被锁定的金额  staing staking_reduction  
     *       "redeemStatus":"1",       //赎回状态， 1： 退回中   2：退回成功 
     *       "redeemUnLockedBlock":"", //预计赎回到账的区块  staing (staking_reduction_epoch+节点质押退回锁定周期（是否包含当前）)*结算周期区块数(C)
     *       "proposalUrl":"",         //提案的github地址  https://github.com/ethereum/EIPs/blob/master/EIPS/eip-100.md  eip-100为提案id
     *       "proposalHash":"",        //提案id
     *       "proposalOption":"",      //投票  1：文本提案    2：升级提案   3：参数提案
     *       "proposalNewVersion":"",  //升级提案的版本
     *       "declareVersion":"",      //声明的版本 
     *       "txReceiptStatus":"",     //交易状态 1:成功 2：失败
     *       "evidence":"",   //证据
     *       "reportType":"",//举报类型:1：区块双签
     *       "reportRewards":"",//举报奖励
     *       "reportStatus":"",//举报状态 \r\n1：失败\r\n2：成功
     *       "pipNum":"",//提案pip编号
     *       "proposalStatus":"",//提案状态\r\n1：投票中\r\n2：通过\r\n3：失败\r\n4：预升级\r\n5：升级完成
     *       "proposalTitle":"",//提案标题
     *        --可选信息结束
     * }
     */	
	@ApiOperation(value = "transaction/transactionDetails", nickname = "", notes = "", response = TransactionListResp.class, tags = { "Transaction" })
	@RequestMapping(value = "transaction/transactionDetails", produces = { "application/json" }, method = RequestMethod.POST)
    public BaseResp<TransactionDetailsResp> transactionDetails(@ApiParam(value = "TransactionDetailsReq ", required = true)@Valid @RequestBody TransactionDetailsReq req);
	
    /**
     * @api {post} transaction/transactionDetailNavigate f.交易详情前后跳转浏览
     * @apiVersion 1.0.0
     * @apiName transactionDetailNavigate
     * @apiGroup transaction
     * @apiDescription 同 《交易详情接口》
     * @apiParamExample {json} Request-Example:
     * {
     *    "txHash":""                  //交易Hash(必填)
     *    "direction":""               //方向：prev-上一个，next-下一个 (必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * > 返回值同《交易详情接口》返回值
     */
	@ApiOperation(value = "transaction/transactionDetailNavigate", nickname = "", notes = "", response = TransactionListResp.class, tags = { "Transaction" })
	@RequestMapping(value = "transaction/transactionDetailNavigate", produces = { "application/json" }, method = RequestMethod.POST)
    public BaseResp<TransactionListResp> transactionDetailNavigate(@ApiParam(value = "TransactionDetailNavigateReq req ", required = true)@Valid @RequestBody TransactionDetailNavigateReq req);
	
}
