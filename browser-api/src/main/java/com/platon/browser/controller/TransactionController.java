package com.platon.browser.controller;

import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.account.AccountDetail;
import com.platon.browser.dto.account.AccountDownload;
import com.platon.browser.dto.account.AddressDetail;
import com.platon.browser.dto.account.ContractDetail;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.transaction.*;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.enums.TransactionTypeEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.ResponseException;
import com.platon.browser.req.account.AccountDownloadReq;
import com.platon.browser.req.account.AddressDetailReq;
import com.platon.browser.req.transaction.*;
import com.platon.browser.res.BaseResp;
import com.platon.browser.service.*;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * User: dongqile
 * Date: 2018/10/23
 * Time: 9:40
 */
@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private static Logger logger = LoggerFactory.getLogger(TransactionController.class);
    @Autowired
    private I18nUtil i18n;
    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private ExportService exportService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private PendingTxService pendingTxService;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionMapper transactionMapper;

    /**
      * @api {post} transaction/transactionList a.交易列表
      * @apiVersion 1.0.0
      * @apiName transactionList
      * @apiGroup transaction
      * @apiDescription 交易列表
      * @apiParamExample {json} Request-Example:
      * {
      *      "cid":"", // 链ID (必填)
      *      "pageNo": 1,//页数(必填)
      *      "pageSize": 10,//页大小(必填)
      * }
      * @apiSuccessExample {json} Success-Response:
      * HTTP/1.1 200 OK
      * {
      *      "errMsg": "",//描述信息
      *      "code": 0,//成功（0），失败则由相关失败码
      *      "displayTotalCount":18,//显示总数
      *      "totalCount":18,// 小于等于500000记录的总数
      *      "totalPages":1,//总页数
      *      "data": [
      *           {
      *           "txHash": "0x234234",//交易hash
      *           "blockHeight": "15566",//交易所在区块高度
      *           "blockTime": 18080899999,//出块时间
      *           "from": "0x667766",//发送方, 必定是钱包地址
      *           "to": "0x667766",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：
      *                            // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址
      *           "value": "222",//数额(单位:Energon)
      *           "actualTxCost": "22",//交易费用(单位:Energon)
      *           "txReceiptStatus": 1,//交易状态 -1 pending 1 成功  0 失败
      *           "txType": "", // 交易类型
      *                   transfer ：转账
      *                   MPCtransaction ： MPC交易
      *                   contractCreate ： 合约创建
      *                   voteTicket ： 投票
      *                   transactionExecute ： 合约执行
      *                   authorization ： 权限
      *                   candidateDeposit ： 竞选质押
      *                   candidateApplyWithdraw ： 减持质押
      *                   candidateWithdraw ： 提取质押
      *                   unknown ： 未知
      *           "txInfo": "{
      *                  "functionName":"",//方法名称
      *                  "parameters":{},//参数
      *                  "type":"1"//交易类型
      *                      0：转账
      *                      1：合约发布
      *                      2：合约调用
      *                      4：权限
      *                      5：MPC交易
      *                      1000：投票
      *                      1001：竞选质押
      *                      1002：减持质押
      *                      1003：提取质押
      *                  }"//返回交易解析结构
      *           }
      *           "serverTime": 1123123,//服务器时间
      *           "failReason":"",//失败原因
      *           "receiveType":"account" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址，
      *                                  // 前端页面在点击接收方的地址时，根据此字段来决定是跳转到账户详情还是合约详情
      *           }
      *       ]
      * }
     */
    @PostMapping("transactionList")
    public RespPage<TransactionListItem> getPage (@Valid @RequestBody TransactionPageReq req) {
        if(!chainsConfig.isValid(req.getCid())){
            throw new ResponseException(i18n.i(I18nEnum.CHAIN_ID_ERROR,req.getCid()));
        }
        RespPage<TransactionListItem> page = redisCacheService.getTransactionPage(req.getCid(),req.getPageNo(),req.getPageSize());
        return page;
    }

    /**
     * @api {post} transaction/transactionDetails b.交易详情
     * @apiVersion 1.0.0
     * @apiName transactionDetails
     * @apiGroup transaction
     * @apiDescription 交易详情
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "txHash": "",//交易Hash(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": {
     *           "txHash": "0x234234",//交易hash
     *           "timestamp": 123123123879,//交易时间
     *           "txReceiptStatus": 1,//交易状态 -1 pending 1 成功  0 失败
     *           "blockHeight": "15566",//交易所在区块高度
     *           "confirmNum":444, // 区块确认数
     *           "from": "0x667766",//发送者
     *           "to": "0x667766",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：
     *                        // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址
     *           "txType": "", // 交易类型
     *              transfer ：转账
     *              MPCtransaction ： MPC交易
     *              contractCreate ： 合约创建
     *              voteTicket ： 投票
     *              transactionExecute ： 合约执行
     *              authorization ： 权限
     *              candidateDeposit ： 竞选质押
     *              candidateApplyWithdraw ： 减持质押
     *              candidateWithdraw ： 提取质押
     *              unknown ： 未知
     *           "value": "222",//数额(单位:Energon)
     *           "actualTxCost": "22",//实际交易手续费(单位:Energon)
     *           "energonLimit": 232,//能量限制
     *           "energonUsed": 122,//能量消耗
     *           "priceInE":"1000000000000000000", // 能量价格(单位:E)
     *           "priceInEnergon":"0.1", // 能量价格(单位:Energon)
     *           "inputData": "",//附加输入数据
     *           "expectTime": 12312333, // 预计确认时间
     *           "failReason":"",//失败原因
     *           "first":false, // 是否第一条记录
     *           "last":true // 是否最后一条记录
     *           "receiveType":"account" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址
     *           "txInfo": "{
     *                  "functionName":"",//方法名称
     *                  "parameters":{},//参数
     *                  "type":"1"//交易类型
     *                      0：转账
     *                      1：合约发布
     *                      2：合约调用
     *                      4：权限
     *                      5：MPC交易
     *                      1000：投票
     *                      1001：竞选质押
     *                      1002：减持质押
     *                      1003：提取质押
     *                  }"//返回交易解析结构
     *           }
     *           "nodeName",""//节点名称（只有type=1000，1001，1002，1003时候，该字段才有值）
     *           "nodeId",""//节点Id（只有type=1000，1001，1002，1003时候，该字段才有值）
     *           "voteCount",33// 投票数（只有投票交易此字段才有值）
     *           "deposit":445,//质押金 (竞选交易此字段才有值)
     *           "ticketPrice":3333 // 票价
     * }
     */
    @PostMapping("transactionDetails")
    public BaseResp getDetail (@Valid @RequestBody TransactionDetailReq req) {
        if(!chainsConfig.isValid(req.getCid())){
            throw new ResponseException(i18n.i(I18nEnum.CHAIN_ID_ERROR,req.getCid()));
        }
        try{
            TransactionDetail transactionDetail = transactionService.getDetail(req);
            setupConfirmNum(transactionDetail,req.getCid());
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),transactionDetail);
        }catch (BusinessException be){
            throw new ResponseException(be.getMessage());
        }
    }

    /**
     * @api {post} transaction/transactionDetailNavigate c.交易详情前后跳转浏览
     * @apiVersion 1.0.0
     * @apiName transactionDetailNavigate
     * @apiGroup transaction
     * @apiDescription 交易详情前后跳转浏览
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "direction":"", // 方向：prev-上一个，next-下一个 (必填)
     *      "txHash": "",//交易Hash(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": {
     *           "txHash": "0x234234",//交易hash
     *           "timestamp": 123123123879,//交易接收时间
     *           "txReceiptStatus": 1,//交易状态 -1 pending 1 成功  0 失败
     *           "blockHeight": "15566",//交易所在区块高度,
     *           "from": "0x667766",//发送者
     *           "to": "0x667766",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：
     *                            // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址
     *           "txType": "", // 交易类型
     *              transfer ：转账
     *              MPCtransaction ： MPC交易
     *              contractCreate ： 合约创建
     *              voteTicket ： 投票
     *              transactionExecute ： 合约执行
     *              authorization ： 权限
     *              authorization ： 权限
     *              candidateDeposit ： 竞选质押
     *              candidateApplyWithdraw ： 减持质押
     *              candidateWithdraw ： 提取质押
     *              unknown ： 未知
     *           "value": "222",//数额(单位:Energon)
     *           "actualTxCost": "22",//实际交易手续费(单位:Energon)
     *           "energonLimit": 232,//能量限制
     *           "energonUsed": 122,//能量消耗
     *           "priceInE":"1000000000000000000", // 能量价格(单位:E)
     *           "priceInEnergon":"0.1", // 能量价格(单位:Energon)
     *           "inputData": "",//附加输入数据
     *           "expectTime": 12312333, // 预计确认时间
     *           "first":false, // 是否第一条记录
     *           "last":true, // 是否最后一条记录
     *           "receiveType":"account" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址
     *           "txInfo": "{
     *                  "functionName":"",//方法名称
     *                  "parameters":{},//参数
     *                  "type":"1"//交易类型
     *                      0：转账
     *                      1：合约发布
     *                      2：合约调用
     *                      4：权限
     *                      5：MPC交易
     *                      1000：投票
     *                      1001：竞选质押
     *                      1002：减持质押
     *                      1003：提取质押
     *                  }"//返回交易解析结构
     *           }
     *           "nodeName",""//节点名称（只有type=1000，1001，1002，1003时候，该字段才有值）
     *           "nodeId",""//节点Id（只有type=1000，1001，1002，1003时候，该字段才有值）
     *           "voteCount",33// 投票数（只有投票交易此字段才有值）
     *           "deposit":445,//质押金 (竞选交易此字段才有值)
     *     }
     * }
     */
    @PostMapping("transactionDetailNavigate")
    public BaseResp transactionDetailNavigate (@Valid @RequestBody TransactionDetailNavigateReq req) {
        if(!chainsConfig.isValid(req.getCid())){
            throw new ResponseException(i18n.i(I18nEnum.CHAIN_ID_ERROR,req.getCid()));
        }
        try{
            TransactionDetail transactionDetail = transactionService.getTransactionDetailNavigate(req);
            setupConfirmNum(transactionDetail,req.getCid());
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),transactionDetail);
        }catch (BusinessException be){
            throw new ResponseException(be.getMessage());
        }
    }

    private void setupConfirmNum(TransactionDetail transactionDetail, String chainId){
        // 设置区块确认数
        // 为加快速度，从缓存获取
        RespPage<BlockListItem> page = redisCacheService.getBlockPage(chainId,1,1);
        if(page.getData()==null||page.getData().size()==0){
            transactionDetail.setConfirmNum(0l);
            return;
        }
        BlockListItem block = page.getData().get(0);
        transactionDetail.setConfirmNum(block.getHeight()-transactionDetail.getBlockHeight());
    }

    /**
     * @api {post} transaction/pendingList d.待处理交易列表
     * @apiVersion 1.0.0
     * @apiName pendingList
     * @apiGroup transaction
     * @apiDescription 待处理交易列表
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "pageNo": 1,//页数(必填)
     *      "pageSize": 10,//页大小(必填),
     *      "address":"0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4", // 地址(可选)，用于筛选功能
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "totalCount":18,//总数
     *      "totalPages":1,//总页数
     *      "data": [
     *           {
     *           "txHash": "0x234234",//交易hash
     *           "timestamp": 33,// 交易接收时间
     *           "energonLimit": 55555,//能量限制
     *           "priceInE":"1000000000000000000", // 能量价格(单位:E)
     *           "priceInEnergon":"0.1", // 能量价格(单位:Energon)
     *           "from": "0x667766",//发送方
     *           "to": "0x667766",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：
     *                            // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址
     *           "value": "222",//数额(单位:Energon)
     *           "txType": "", // 交易类型
     *                  transfer ：转账
     *                  MPCtransaction ： MPC交易
     *                  contractCreate ： 合约创建
     *                  voteTicket ： 投票
     *                  transactionExecute ： 合约执行
     *                  authorization ： 权限
     *                  authorization ： 权限
     *                  candidateDeposit ： 竞选质押
     *                  candidateApplyWithdraw ： 减持质押
     *                  candidateWithdraw ： 提取质押
     *                  unknown ： 未知
     *           "serverTime": 1123123,//服务器时间
     *           "receiveType":"account" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址
     *           "txInfo": "{
     *                  "functionName":"",//方法名称
     *                  "parameters":{},//参数
     *                  "type":"1"//交易类型
     *                      0：转账
     *                      1：合约发布
     *                      2：合约调用
     *                      4：权限
     *                      5：MPC交易
     *                      1000：投票
     *                      1001：竞选质押
     *                      1002：减持质押
     *                      1003：提取质押
     *                  }"//返回交易解析结构
     *           }
     *           }
     *       ]
     * }
     */
    @PostMapping("pendingList")
    public RespPage<PendingTxItem> pendingList (@Valid @RequestBody PendingTxPageReq req) {
        if(!chainsConfig.isValid(req.getCid())){
            throw new ResponseException(i18n.i(I18nEnum.CHAIN_ID_ERROR,req.getCid()));
        }
        RespPage<PendingTxItem> returnData = pendingTxService.getTransactionList(req);
        return returnData;
    }


    /**
     * @api {post} transaction/pendingDetails e.待处理交易详情
     * @apiVersion 1.0.0
     * @apiName pendingDetails
     * @apiGroup transaction
     * @apiDescription 待处理交易详情
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "txHash": "",//交易Hash(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK

     *  {
     *      "errMsg": "成功",
     *      "code": 0,
     *      "data": {
     *          "type": "pending", // 结果类型:pending-待处理，transaction-已处理交易；
     *                              // 原因：待处理交易可能随时在变化，在查询开始前可能此笔交易已被处理，所以这里需要添加结果类型作为区分;
     *                              // 前端页面处理时，如果发现此值为transaction，则需要使用交易hash作为参数跳转到交易详情页面
     *          "pending": { // 待处理交易数据，当type的值为transaction，此字段为空
     *              "txHash": "0x234234",//交易hash
     *              "timestamp": 123123123879,//交易接收时间
     *              "txReceiptStatus": 1,//交易状态 -1 pending 1 成功  0 失败
     *              "blockHeight": "15566",//交易所在区块高度
     *              "from": "0x667766",//发送者
     *              "to": "0x667766",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：
     *                               // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址
     *              "txType": "", // 交易类型
     *                  transfer ：转账
     *                  MPCtransaction ： MPC交易
     *                  contractCreate ： 合约创建
     *                  voteTicket ： 投票
     *                  transactionExecute ： 合约执行
     *                  authorization ： 权限
     *                  authorization ： 权限
     *                  candidateDeposit ： 竞选质押
     *                  candidateApplyWithdraw ： 减持质押
     *                  candidateWithdraw ： 提取质押
     *                  unknown ： 未知
     *              "value": "222",//数额(单位:Energon)
     *              "actualTxCost": "22",//实际交易手续费(单位:Energon)
     *              "energonLimit": 232,//能量限制
     *              "energonUsed": 122,//能量消耗
     *              "priceInE":"1000000000000000000", // 能量价格(单位:E)
     *              "priceInEnergon":"0.1", // 能量价格(单位:Energon)
     *              "inputData": "",//附加输入数据
     *              "expectTime": 12312333, // 预计确认时间
     *              "receiveType":"account" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址
     *              "txInfo": "{
     *                     "functionName":"",//方法名称
     *                     "parameters":{},//参数
     *                     "type":"1"//交易类型
     *                         0：转账
     *                         1：合约发布
     *                         2：合约调用
     *                         4：权限
     *                         5：MPC交易
     *                         1000：投票
     *                         1001：竞选质押
     *                         1002：减持质押
     *                         1003：提取质押
     *                     }"//返回交易解析结构
     *              }
     *              "nodeName",""//节点名称（只有type=1000，1001，1002，1003时候，该字段才有值）
     *              "nodeId",""//节点Id（只有type=1000，1001，1002，1003时候，该字段才有值）
     *              "voteCount",33// 投票数（只有投票交易此字段才有值）
     *              "deposit":445,//质押金 (竞选交易此字段才有值)
     *          }
     *      }
     *  }
     */
    @PostMapping("pendingDetails")
    public BaseResp pendingDetails (@Valid @RequestBody PendingTxDetailReq req) {
        if(!chainsConfig.isValid(req.getCid())){
            throw new ResponseException(i18n.i(I18nEnum.CHAIN_ID_ERROR,req.getCid()));
        }
        try{
            PendingOrTransaction pendingOrTransaction = pendingTxService.getDetail(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),pendingOrTransaction);
        }catch (BusinessException be){
            throw new ResponseException(be.getMessage());
        }
    }

    /**
     * @api {post}  g.查询地址详情
     * @apiVersion 1.0.0
     * @apiName addressDetails
     * @apiGroup transaction
     * @apiDescription 查询地址详情
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "address": "0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4",// 账户地址(必填)
     *      "txType":"", // 交易类型 (可选), 可以设置多个类型，使用逗号“,”分隔
     *           transfer ：转账
     *           MPCtransaction ： MPC交易
     *           contractCreate ： 合约创建
     *           voteTicket ： 投票
     *           transactionExecute ： 合约执行
     *           authorization ： 权限
     *           candidateDeposit ： 竞选质押
     *           candidateApplyWithdraw ： 减持质押
     *           candidateWithdraw ： 提取质押
     *           unknown ： 未知
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": {
     *         "balance":131,165,156.62618849461651616321, // 余额(单位:Energon)
     *         "tradeCount":236, // 交易数
     *         "votePledge":131,165,156.62618, // 投票质押
     *         "nodeCount":3, // 投票节点数
     *         "trades":[
     *            {
     *                "txHash": "0x234234",//交易hash
     *                "blockTime": 18080899999,//确认时间(出块时间)
     *                "from": "0x667766",//发送方
     *                "to": "0x667766",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：
     *                                  // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址
     *                "value": "222",//数额(单位:Energon)
     *                "actualTxCost": "22",//交易费用(单位:Energon)
     *                "txReceiptStatus": 1,//交易状态 -1 pending 1 成功  0 失败
     *                "txType": "", // 交易类型
     *                    transfer ：转账
     *                    MPCtransaction ： MPC交易
     *                    contractCreate ： 合约创建
     *                    voteTicket ： 投票
     *                    transactionExecute ： 合约执行
     *                    authorization ： 权限
     *                    candidateDeposit ： 竞选质押
     *                    candidateApplyWithdraw ： 减持质押
     *                    candidateWithdraw ： 提取质押
     *                    unknown ： 未知
     *                "txInfo":"", // 交易参数
     *                "serverTime": 1123123,//服务器时间
     *                "failReason":"",//失败原因
     *                "receiveType":"account" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址
     *                "nodeId": "", // 节点ID
     *                "nodeName",""//节点名称（只有type=1000，1001，1002，1003时候，该字段才有值）
     *                "deposit":"",//质押金 (竞选交易此字段才有值)
     *                ---------以下字段只有txType=voteTicket有效----------
     *                "ticketPrice":"",//票价
     *                "voteCount":"",//投票数
     *                "validVoteCount":33,//有效票
     *                "income":"",//收益
     *             }
     *          ]
     *      }
     * }
     */
    @PostMapping("addressDetails")
    public BaseResp addressDetails (@Valid @RequestBody AddressDetailReq req) {
        if(!chainsConfig.isValid(req.getCid())){
            throw new ResponseException(i18n.i(I18nEnum.CHAIN_ID_ERROR,req.getCid()));
        }
        try{
            req.setPageSize(20);
            AddressDetail initData = accountService.getAddressDetail(req);
            AccountDetail returnData = accountService.getAccountDetail(req.getAddress(),req.getCid());
            returnData.init(initData);
            List<AccTransactionItem> transactions = initData.getTrades();
            if(transactions.size()>20){
                // 大于20，则取前20条数据返回
                returnData.setTrades(transactions.subList(0,20));
            }
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),returnData);
        }catch (BusinessException be){
            throw new ResponseException(be.getMessage());
        }
    }

    private void download(HttpServletResponse response, String filename, long length, byte [] data){
        response.setHeader("Content-Disposition", "attachment; filename="+filename);
        response.setContentType("application/octet-stream");
        response.setContentLengthLong(length);
        try {
            response.getOutputStream().write(data);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new ResponseException(i18n.i(I18nEnum.DOWNLOAD_EXCEPTION));
        }
    }
    /**
     * @api {get} transaction/addressDownload?cid=:cid&address=:address&date=:date h.导出地址详情
     * @apiVersion 1.0.0
     * @apiName addressDownload
     * @apiGroup transaction
     * @apiDescription 导出地址详情
     * @apiParam {String} cid 链ID
     * @apiParam {String} address 合约地址
     * @apiParam {String} date 数据结束日期
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * 响应为 二进制文件流
     */
    @GetMapping("addressDownload")
    public void addressDownload(@RequestParam String cid, @RequestParam String address, @RequestParam String date, HttpServletResponse response) {
        AccountDownloadReq req = new AccountDownloadReq();
        req.setCid(cid);
        req.setAddress(address);
        try {
            SimpleDateFormat ymd = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat ymdhms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = ymd.parse(date);
            String startStr = ymd.format(startDate);
            req.setStartDate(ymdhms.parse(startStr+" 00:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ResponseException(i18n.i(I18nEnum.FORMAT_DATE_ERROR));
        }
        req.setEndDate(new Date());
        AccountDownload accountDownload = exportService.exportAccountCsv(req);
        download(response,accountDownload.getFilename(),accountDownload.getLength(),accountDownload.getData());
    }

    /**
     * @api {post} transaction/contractDetails i.查询合约详情
     * @apiVersion 1.0.0
     * @apiName contractDetails
     * @apiGroup transaction
     * @apiDescription 查询合约详情
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "address": "0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4",// 账户地址(必填)
     *      "txType":"", // 交易类型 (可选)
     *           voteTicket ： 投票
     *           transactionExecute ： 合约执行
     *           authorization ： 权限
     *           candidateDeposit ： 竞选质押
     *           candidateApplyWithdraw ： 减持质押
     *           candidateWithdraw ： 提取质押
     *           unknown ： 未知
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": {
     *          "balance":131,165,156.62618849461651616321, // 余额(单位:Energon)
                "tradeCount":236, // 交易数
                "developer":131,165,156.62618, // 合约开发者
                "ownerCount":3, // 合约拥有者
     *          "trades":[
     *                 {
     *                 "txHash": "0x234234",//交易hash
     *                 "blockTime": 18080899999,//确认时间(出块时间)
     *                 "from": "0x667766",//发送方
     *                 "to": "0x667766",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：
     *                                 // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址
     *                 "value": "222",//数额(单位:Energon)
     *                 "actualTxCost": "22",//交易费用(单位:Energon)
     *                 "txReceiptStatus": 1,//交易状态 -1 pending 1 成功  0 失败
     *                 "txType": "", // 交易类型
     *                     transfer ：转账
     *                     MPCtransaction ： MPC交易
     *                     contractCreate ： 合约创建
     *                     voteTicket ： 投票
     *                     transactionExecute ： 合约执行
     *                     authorization ： 权限
     *                 "txInfo":"", // 交易参数信息
     *                 "serverTime": 1123123,//服务器时间
     *                 "failReason":"",//失败原因
     *                 "receiveType":"account" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址
     *                 "ticketPrice": null, // 投票交易此字段才有值
     *                 "income": 0, // 投票交易此字段才有值
     *                 "voteCount": 0, // 投票交易此字段才有值
     *                 "deposit": 1200000, // 质押金，竞选交易此字段才有值
     *                 "nodeId": "", // 节点ID
     *                 "nodeName": "Platon-Beijing" // 节点名称
     *                 }
     *             ]
     *      }
     * }
     */
    @PostMapping("contractDetails")
    public BaseResp contractDetails (@Valid @RequestBody AddressDetailReq req) {
        if(!chainsConfig.isValid(req.getCid())){
            throw new ResponseException(i18n.i(I18nEnum.CHAIN_ID_ERROR,req.getCid()));
        }
        try{
            req.setPageSize(20);
            AddressDetail initData = accountService.getAddressDetail(req);

            ContractDetail returnData = new ContractDetail();

            // 取合约开发者地址，取创建合约交易中的from地址
            TransactionExample trEx = new TransactionExample();
            trEx.createCriteria().andChainIdEqualTo(req.getCid()).andFromEqualTo(req.getAddress()).andTxTypeEqualTo(TransactionTypeEnum.TRANSACTION_CONTRACT_CREATE.code);
            List<Transaction> trans = transactionMapper.selectByExample(trEx);
            if(trans.size()!=0){
                Transaction transaction = trans.get(0);
                returnData.setDeveloper(transaction.getFrom());
            }
            returnData.setOwnerCount(1);
            returnData.init(initData);
            List<AccTransactionItem> transactions = initData.getTrades();
            if(transactions.size()>20){
                // 大于20，则取前20条数据返回
                returnData.setTrades(transactions.subList(0,20));
            }
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),returnData);
        }catch (BusinessException be){
            throw new ResponseException(be.getMessage());
        }
    }

    /**
     * @api {get} transaction/contractDownload?cid=:cid&address=:address&date=:date j.导出合约详情
     * @apiVersion 1.0.0
     * @apiName contractDownload
     * @apiGroup transaction
     * @apiDescription 导出合约详情
     * @apiParam {String} cid 链ID
     * @apiParam {String} address 合约地址
     * @apiParam {String} date 数据起始日期
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * 响应为 二进制文件流
     */
    @GetMapping("contractDownload")
    public void contractDownload(@RequestParam String cid, @RequestParam String address, @RequestParam String date, HttpServletResponse response) {
        addressDownload(cid,address,date,response);
    }

    /**
     * @api {get} transaction/blockTransaction k.查询区块交易信息
     * @apiVersion 1.0.0
     * @apiName blockTransaction
     * @apiGroup transaction
     * @apiDescription 查询区块交易信息
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "pageNo": 1,//页数(必填)
     *      "pageSize": 10,//页大小(必填),
     *      "height": 123,//区块高度(必填)
     *      "txType": "", // 交易类型
                    trancaction ：交易
                    voteTicket ： 投票
                    candidateDeposit ： 竞选
     * }
     * @apiSuccessExample {json} Success-Response:
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "totalCount":18,//总数
     *      "totalPages":1,//总页数
     *      "data": [
     *           {
     *           "txHash": "0x234234",//交易hash
     *           "blockHeight": "15566",//交易所在区块高度
     *           "blockTime": 18080899999,//出块时间
     *           "from": "0x667766",//发送方
     *           "to": "0x667766",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：
     *                            // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址
     *           "value": "222",//数额(单位:Energon)
     *           "actualTxCost": "22",//交易费用(单位:Energon)
     *           "txReceiptStatus": 1,//交易状态 -1 pending 1 成功  0 失败
     *           "txType": "", // 交易类型
                    transfer ：转账
                    MPCtransaction ： MPC交易
                    contractCreate ： 合约创建
                    voteTicket ： 投票
                    transactionExecute ： 合约执行
                    authorization ： 权限
     *           "serverTime": 1123123,//服务器时间
     *           "failReason":"",//失败原因
     *           "receiveType":"account" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址
     *           "txInfo": "{
     *                  "functionName":"",//方法名称
     *                  "parameters":{},//参数
     *                  "type":"1"//交易类型
     *                      0：转账
     *                      1：合约发布
     *                      2：合约调用
     *                      4：权限
     *                      5：MPC交易
     *                      1000：投票
     *                      1001：竞选质押
     *                      1002：减持质押
     *                      1003：提取质押
     *                  }"//返回交易解析结构
     *           }
     *           "nodeName",""//节点名称（只有type=1000，1001，1002，1003时候，该字段才有值）
     *
     *       ]
     * }
     */
    @PostMapping("blockTransaction")
    public RespPage<TransactionListItem> blockTransaction (@Valid @RequestBody BlockTransactionListReq req) {
        if(!chainsConfig.isValid(req.getCid())){
            throw new ResponseException(i18n.i(I18nEnum.CHAIN_ID_ERROR,req.getCid()));
        }
        TransactionPageReq tlr = new TransactionPageReq();
        BeanUtils.copyProperties(req,tlr);
        RespPage<TransactionListItem> returnData = transactionService.getPageByBlockNumber(tlr);
        return returnData;
    }
}