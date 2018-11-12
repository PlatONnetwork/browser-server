package com.platon.browser.controller;

import com.platon.browser.common.base.BaseResp;
import com.platon.browser.common.base.JsonResp;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.dto.account.AccountDetail;
import com.platon.browser.dto.account.AccountDowload;
import com.platon.browser.dto.account.ContractDetail;
import com.platon.browser.dto.account.ContractDowload;
import com.platon.browser.dto.transaction.*;
import com.platon.browser.exception.ResponseException;
import com.platon.browser.req.account.AccountDetailReq;
import com.platon.browser.req.account.AccountDownloadReq;
import com.platon.browser.req.account.ContractDetailReq;
import com.platon.browser.req.account.ContractDownloadReq;
import com.platon.browser.req.transaction.*;
import com.platon.browser.service.*;
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
    private TransactionService transactionService;

    @Autowired
    private PendingTxService pendingTxService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ContractService contractService;

    @Autowired
    private ExportService exportService;

    /**
     * @api {post} transaction/transactionList a.交易列表
     * @apiVersion 1.0.0
     * @apiName transactionList
     * @apiGroup transaction
     * @apiDescription 交易列表
     * @apiParam {String} cid 链ID.
     * @apiUse CommonHeaderFiled
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
     *      "totalCount":18,//总数
     *      "totalPages":1,//总页数
     *      "data": [
     *           {
     *           "txHash": "0x234234",//交易hash
     *           "blockHeight": "15566",//交易所在区块高度
     *           "blockTime": 18080899999,//出块时间
     *           "from": "0x667766",//发送方
     *           "to": "0x667766",//接收方
     *           "value": "222",//数额
     *           "actualTxCost": "22",//交易费用
     *           "txReceiptStatus": 1,//交易状态 -1 pending 1 成功  0 失败
     *           "txType": "", // 交易类型
                        transfer ：转账
                        MPCtransaction ： MPC交易
                        contractCreate ： 合约创建
                        vote ： 投票
                        transactionExecute ： 合约执行
                        authorization ： 权限
     *           "serverTime": 1123123,//服务器时间
     *           "failReason":""//失败原因
     *           }
     *       ]
     * }
     */
    @PostMapping("transactionList")
    public JsonResp transactionList (@Valid @RequestBody TransactionListReq req ) {
        req.buildPage();
        List<TransactionItem> transactionListList = transactionService.getTransactionList(req);
        return JsonResp.asList().addAll(transactionListList).pagination(req).build();
    }

    /**
     * @api {post} transaction/transactionDetails b.交易详情
     * @apiVersion 1.0.0
     * @apiName transactionDetails
     * @apiGroup transaction
     * @apiDescription 交易详情
     * @apiParam {String} cid 链ID.
     * @apiUse CommonHeaderFiled
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
     *           "to": "0x667766",//接收者
     *           "txType": "", // 交易类型
                    transfer ：转账
                    MPCtransaction ： MPC交易
                    contractCreate ： 合约创建
                    vote ： 投票
                    transactionExecute ： 合约执行
                    authorization ： 权限
     *           "value": "222",//数额
     *           "actualTxCost": "22",//实际交易手续费
     *           "energonLimit": 232,//能量限制
     *           "energonUsed": 122,//能量消耗
     *           "energonPrice": "123",//能量价格
     *           "inputData": "",//附加输入数据
     *           "expectTime": 12312333, // 预计确认时间
     *           "failReason":""//失败原因
     *           }
     * }
     */
    @PostMapping("transactionDetails")
    public BaseResp transactionDetails (@Valid @RequestBody TransactionDetailReq req) {
        try{
            TransactionDetail transactionDetail = transactionService.getTransactionDetail(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),transactionDetail);
        }catch (BusinessException be){
            return BaseResp.build(be.getErrorCode(),be.getErrorMessage(),null);
        }
    }

    /**
     * @api {post} transaction/transactionDetailNavigate c.交易详情前后跳转浏览
     * @apiVersion 1.0.0
     * @apiName transactionDetailNavigate
     * @apiGroup transaction
     * @apiDescription 交易详情前后跳转浏览
     * @apiParam {String} cid 链ID.
     * @apiUse CommonHeaderFiled
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
     *           "to": "0x667766",//接收者
     *           "txType": "", // 交易类型
                    transfer ：转账
                    MPCtransaction ： MPC交易
                    contractCreate ： 合约创建
                    vote ： 投票
                    transactionExecute ： 合约执行
                    authorization ： 权限
     *           "value": "222",//数额
     *           "actualTxCost": "22",//实际交易手续费
     *           "energonLimit": 232,//能量限制
     *           "energonUsed": 122,//能量消耗
     *           "energonPrice": "123",//能量价格
     *           "inputData": "",//附加输入数据
     *           "expectTime": 12312333, // 预计确认时间
     *           }
     * }
     */
    @PostMapping("transactionDetailNavigate")
    public BaseResp transactionDetailNavigate (@Valid @RequestBody TransactionDetailNavigateReq req) {
        try{
            TransactionDetailNavigate transactionDetailNavigate = transactionService.getTransactionDetailNavigate(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),transactionDetailNavigate);
        }catch (BusinessException be){
            return BaseResp.build(be.getErrorCode(),be.getErrorMessage(),null);
        }
    }

    /**
     * @api {post} transaction/pendingList d.待处理交易列表
     * @apiVersion 1.0.0
     * @apiName pendingList
     * @apiGroup transaction
     * @apiDescription 待处理交易列表
     * @apiParam {String} cid 链ID.
     * @apiUse CommonHeaderFiled
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
     *           "energonPrice": 55555,//能量价格
     *           "from": "0x667766",//发送方
     *           "to": "0x667766",//接收方
     *           "value": "222",//数额
     *           "txType": "", // 交易类型
*                         transfer ：转账
*                         MPCtransaction ： MPC交易
*                         contractCreate ： 合约创建
*                         vote ： 投票
*                         transactionExecute ： 合约执行
*                         authorization ： 权限
     *           "serverTime": 1123123,//服务器时间
     *           }
     *       ]
     * }
     */
    @PostMapping("pendingList")
    public JsonResp pendingList (@Valid @RequestBody PendingTxListReq req ) {
        req.buildPage();
        List<PendingTxItem> pendingTxList = pendingTxService.getTransactionList(req);
        return JsonResp.asList().addAll(pendingTxList).pagination(req).build();
    }


    /**
     * @api {post} transaction/pendingDetails e.待处理交易详情
     * @apiVersion 1.0.0
     * @apiName pendingDetails
     * @apiGroup transaction
     * @apiDescription 待处理交易详情
     * @apiParam {String} cid 链ID.
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "txHash": "",//交易Hash(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
    {
        type:"pending", // 结果类型:pending-待处理，transaction-已处理交易；
                        // 原因：待处理交易可能随时在变化，在查询开始前可能此笔交易已被处理，所以这里需要添加结果类型作为区分;
                        // 前端页面处理时，如果发现此值为transaction，则需要使用交易hash作为参数跳转到交易详情页面
        data:{
            "errMsg": "",//描述信息
            "code": 0,//成功（0），失败则由相关失败码
            "data": {
                "txHash": "0x234234",//交易hash
                "timestamp": 123123123879,//交易接收时间
                "txReceiptStatus": 1,//交易状态 -1 pending 1 成功  0 失败
                "blockHeight": "15566",//交易所在区块高度
                "from": "0x667766",//发送者
                "to": "0x667766",//接收者
                "txType": "", // 交易类型
                    transfer ：转账
                    MPCtransaction ： MPC交易
                    contractCreate ： 合约创建
                    vote ： 投票
                    transactionExecute ： 合约执行
                    authorization ： 权限
                "value": "222",//数额
                "actualTxCost": "22",//实际交易手续费
                "energonLimit": 232,//能量限制
                "energonUsed": 122,//能量消耗
                "energonPrice": "123",//能量价格
                "inputData": "",//附加输入数据
                "expectTime": 12312333, // 预计确认时间
            }
        }
    }
     */
    @PostMapping("pendingDetails")
    public BaseResp pendingDetails (@Valid @RequestBody PendingTxDetailReq req) {
        try{
            PendingOrTransaction pendingOrTransaction = pendingTxService.getTransactionDetail(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),pendingOrTransaction);
        }catch (BusinessException be){
            return BaseResp.build(be.getErrorCode(),be.getErrorMessage(),null);
        }
    }

    /**
     * @api {post} transaction/pendingDetailNavigate f.待处理交易详情前后跳转浏览 (已废弃，因为待处理交易块还未生成，随时在变，前后浏览没有意义)
     * @apiVersion 1.0.0
     * @apiName pendingDetailNavigate
     * @apiGroup transaction
     * @apiDescription 待处理交易详情前后跳转浏览
     * @apiParam {String} cid 链ID.
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "direction":"", // 方向：prev-上一个，next-下一个 (必填)
     *      "index": "",// 当前记录索引，从1开始(必填)
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
     *           "blockHeight": "15566",//交易所在区块高度
     *           "from": "0x667766",//发送者
     *           "to": "0x667766",//接收者
     *           "txType": "", // 交易类型
                    transfer ：转账
                    MPCtransaction ： MPC交易
                    contractCreate ： 合约创建
                    vote ： 投票
                    transactionExecute ： 合约执行
                    authorization ： 权限
     *           "value": "222",//数额
     *           "actualTxCost": "22",//实际交易手续费
     *           "energonLimit": 232,//能量限制
     *           "energonUsed": 122,//能量消耗
     *           "energonPrice": "123",//能量价格
     *           "inputData": "",//附加输入数据
     *           "expectTime": 12312333, // 预计确认时间
     *           }
     * }
     */
    /*@PostMapping("pendingDetailNavigate")
    public BaseResp pendingDetailNavigate (@Valid @RequestBody PendingTxDetailNavigateReq req) {
        try{
            PendingTxDetailNavigate pendingTxDetailNavigate = pendingTxService.getPendingTxDetailNavigate(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),pendingTxDetailNavigate);
        }catch (BusinessException be){
            return BaseResp.build(be.getErrorCode(),be.getErrorMessage(),null);
        }
    }*/

    /**
     * @api {post} transaction/addressDetails g.查询地址详情
     * @apiVersion 1.0.0
     * @apiName addressDetails
     * @apiGroup transaction
     * @apiDescription 查询地址详情
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "address": "0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4",// 账户地址(必填)
     *      "txType":"", // 交易类型 (可选)
                transfer ：转账
                MPCtransaction ： MPC交易
                contractCreate ： 合约创建
                vote ： 投票
                transactionExecute ： 合约执行
                authorization ： 权限
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": {
     *         "balance":131,165,156.62618849461651616321, // 余额
     *         "tradeCount":236, // 交易数
     *         "votePledge":131,165,156.62618, // 投票质押
     *         "nodeCount":3, // 投票节点数
     *         "trades":[
     *                {
     *                "txHash": "0x234234",//交易hash
     *                "blockTime": 18080899999,//确认时间(出块时间)
     *                "from": "0x667766",//发送方
     *                "to": "0x667766",//接收方
     *                "value": "222",//数额
     *                "actualTxCost": "22",//交易费用
     *                "txReceiptStatus": 1,//交易状态 -1 pending 1 成功  0 失败
     *                "txType": "", // 交易类型
                         transfer ：转账
                         MPCtransaction ： MPC交易
                         contractCreate ： 合约创建
                         vote ： 投票
                         transactionExecute ： 合约执行
                         authorization ： 权限
     *                "serverTime": 1123123,//服务器时间
     *                "failReason":""//失败原因
     *                }
     *            ]
     *      }
     * }
     */
    @PostMapping("addressDetails")
    public BaseResp addressDetails (@Valid @RequestBody AccountDetailReq req) {
        try{
            req.setPageSize(20);
            AccountDetail accountDetail = accountService.getAccountDetail(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),accountDetail);
        }catch (BusinessException be){
            return BaseResp.build(be.getErrorCode(),be.getErrorMessage(),null);
        }
    }

    private void download(HttpServletResponse response, String filename,long length, byte [] data){
        response.setHeader("Content-Disposition", "attachment; filename="+filename);
        response.setContentType("application/octet-stream");
        response.setContentLengthLong(length);
        try {
            response.getOutputStream().write(data);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new ResponseException("下载数据异常！");
        }
    }
    /**
     * @api {get} transaction/addressDownload?cid=:cid&address=:address&date=:date h.导出地址详情
     * @apiVersion 1.0.0
     * @apiName addressDownload
     * @apiGroup transaction
     * @apiDescription 导出地址详情
     * @apiUse CommonHeaderFiled
     * @apiParam {String} cid 链ID
     * @apiParam {String} address 合约地址
     * @apiParam {String} date 数据起始日期
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * 响应为 二进制文件流
     */
    @GetMapping("addressDownload")
    public void addressDownload(@RequestParam String cid,@RequestParam String address, @RequestParam String date, HttpServletResponse response) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        AccountDownloadReq req = new AccountDownloadReq();
        req.setCid(cid);
        req.setAddress(address);
        try {
            req.setDate(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ResponseException("日期格式错误！");
        }
        AccountDowload accountDowload = exportService.exportAccountCsv(req);
        download(response,accountDowload.getFilename(),accountDowload.getLength(),accountDowload.getData());
    }

    /**
     * @api {post} transaction/contractDetails i.查询合约详情
     * @apiVersion 1.0.0
     * @apiName contractDetails
     * @apiGroup transaction
     * @apiDescription 查询合约详情
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "address": "0xdE41ad9010ED7ae4a7bBc42b55665151dcc8DEf4",// 账户地址(必填)
     *      "txType":"", // 交易类型 (可选)
                transfer ：转账
                MPCtransaction ： MPC交易
                contractCreate ： 合约创建
                vote ： 投票
                transactionExecute ： 合约执行
                authorization ： 权限
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": {
     *          "balance":131,165,156.62618849461651616321, // 余额
                "tradeCount":236, // 交易数
                "developer":131,165,156.62618, // 合约开发者
                "ownerCount":3, // 合约拥有者
     *          "trades":[
     *                 {
     *                 "txHash": "0x234234",//交易hash
     *                 "blockTime": 18080899999,//确认时间(出块时间)
     *                 "from": "0x667766",//发送方
     *                 "to": "0x667766",//接收方
     *                 "value": "222",//数额
     *                 "actualTxCost": "22",//交易费用
     *                 "txReceiptStatus": 1,//交易状态 -1 pending 1 成功  0 失败
     *                 "txType": "", // 交易类型
                          transfer ：转账
                          MPCtransaction ： MPC交易
                          contractCreate ： 合约创建
                          vote ： 投票
                          transactionExecute ： 合约执行
                          authorization ： 权限
     *                 "serverTime": 1123123,//服务器时间
     *                 "failReason":""//失败原因
     *                 }
     *             ]
     *      }
     * }
     */
    @PostMapping("contractDetails")
    public BaseResp contractDetails (@Valid @RequestBody ContractDetailReq req) {
        try{
            req.setPageSize(Integer.MAX_VALUE);
            ContractDetail contractDetail = contractService.getContractDetail(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),contractDetail);
        }catch (BusinessException be){
            return BaseResp.build(be.getErrorCode(),be.getErrorMessage(),null);
        }
    }

    /**
     * @api {get} transaction/contractDownload?cid=:cid&address=:address&date=:date j.导出合约详情
     * @apiVersion 1.0.0
     * @apiName contractDownload
     * @apiGroup transaction
     * @apiDescription 导出合约详情
     * @apiUse CommonHeaderFiled
     * @apiParam {String} cid 链ID
     * @apiParam {String} address 合约地址
     * @apiParam {String} date 数据起始日期
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * 响应为 二进制文件流
     */
    @GetMapping("contractDownload")
    public void contractDownload(@RequestParam String cid,@RequestParam String address,@RequestParam String date, HttpServletResponse response) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ContractDownloadReq req = new ContractDownloadReq();
        req.setCid(cid);
        req.setAddress(address);
        try {
            req.setDate(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ResponseException("日期格式错误！");
        }
        ContractDowload contractDowload = exportService.exportContractCsv(req);
        download(response,contractDowload.getFilename(),contractDowload.getLength(),contractDowload.getData());
    }

    /**
     * @api {get} transaction/blockTransaction k.查询区块交易信息
     * @apiVersion 1.0.0
     * @apiName blockTransaction
     * @apiGroup transaction
     * @apiDescription 查询区块交易信息
     * @apiUse CommonHeaderFiled
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "pageNo": 1,//页数(必填)
     *      "pageSize": 10,//页大小(必填),
     *      "height": 123,//区块高度(必填)
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
     *           "to": "0x667766",//接收方
     *           "value": "222",//数额
     *           "actualTxCost": "22",//交易费用
     *           "txReceiptStatus": 1,//交易状态 -1 pending 1 成功  0 失败
     *           "txType": "", // 交易类型
                    transfer ：转账
                    MPCtransaction ： MPC交易
                    contractCreate ： 合约创建
                    vote ： 投票
                    transactionExecute ： 合约执行
                    authorization ： 权限
     *           "serverTime": 1123123,//服务器时间
     *           "failReason":""//失败原因
     *           }
     *       ]
     * }
     */
    @PostMapping("blockTransaction")
    public JsonResp blockTransaction (@Valid @RequestBody BlockTransactionListReq req) {
        TransactionListReq tlr = new TransactionListReq();
        BeanUtils.copyProperties(req,tlr);
        tlr.buildPage();
        List<TransactionItem> transactionListList = transactionService.getTransactionList(tlr);
        return JsonResp.asList().addAll(transactionListList).pagination(tlr).build();
    }
}