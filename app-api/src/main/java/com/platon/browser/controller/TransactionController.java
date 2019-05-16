package com.platon.browser.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.req.transaction.TransactionListReq;
import com.platon.browser.service.ApiService;
import com.platon.browser.util.I18nUtil;

/**
 * User: dongqile
 * Date: 2019/3/19
 * Time: 11:39
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
    private ApiService apiService;

    /**
     * @api {post} transaction/list a.通过地址和指定交易序号查询交易列表
     * @apiVersion 1.0.0
     * @apiName transaction/list
     * @apiGroup api
     * @apiDescription 通过地址和指定交易序号查询交易列表
     * @apiParamExample {json} Request-Example:
     * {
     *      "address":"0x...",                   //地址 (必填)
     *      "beginSequence":120,                 //起始序号 (必填)
     *      "listSize":100,                      //列表大小 (必填)
     *      "cid":"",                            //链ID (必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg":"",                         //描述信息
     *      "code":0,                            //成功（0），失败则由相关失败码
     *      "data":[
     *           {
     *             "actualTxCost":"211680",      //交易实际花费值(手续费)，单位：wei
     *             "blockNumber":187566,         //区块高度
     *             "chainId":"203",              //链id
     *             "from":"0x...",               //交易发起方地址
     *             "hash":"0x...",               //交易hash
     *             "sequence":153,               //排列序号：由区块号和交易索引拼接而成
     *             "timestamp":1557484976000,    //交易时间（单位：毫秒）
     *             "to":"0x...",                 //交易接收方地址
     *             "transactionIndex":0,         //交易在区块中位置
     *             "txInfo":"{json}",            //交易详细信息
     *             "txReceiptStatus":1,          //交易状态 1 成功 0 失败
     *             "txType":"transfer",          //交易类型 
     *                                           transfer：转账
     *                                           MPCtransaction：MPC交易
     *                                           contractCreate：合约创建
     *                                           voteTicket：投票
     *                                           transactionExecute：合约执行
     *                                           candidateDeposit：质押
     *                                           candidateApplyWithdraw：减持质押
     *                                           candidateWithdraw：提取质押
     *                                           unknown：其他
     *             "value":"1000000000000000000" //交易金额，单位E
     *           }
     *       ]
     * }
     * 
     * txType = voteTicket时：txInfo信息：
     * {
     *   "functionName":"VoteTicket",            //方法名称
     *   "parameters":{
     *      "price":"100000000000000000001",     //投票时价格，单位E
     *      "count":"19",                        //投票数
     *      "nodeId":"0x...",                    //节点id
     *      "nodeName":"",                       //节点名称
     *      "deposit":"1.254555555"              //质押金(单位:Energon)
     *   },
     *   "type":"1000"                           //交易类型
     *}
     *
     * txType = candidateDeposit时：txInfo信息：
     * {
     *   "functionName":"CandidateDeposit",      //方法名称
     *   "parameters":{
     *       "owner":"0x...",                    //质押的节点钱包
     *       "Extra":"{\"nodeName\":\"TEST-US\",\"officialWebsite\":\"\",\"time\":1555676896325,\"nodePortrait\":\"4\",\"nodeDiscription\":\"A test network node deployed in Los Angeles\",\"nodeDepartment\":\"American test node\"}",
     *       "port":"16792",                     //节点p2p端口
     *       "fee":"2000",                       //出块奖励佣金比，以10000为基数(eg：5%，则fee=500)
     *       "host":"54.67.96.43",               //节点ip
     *       "nodeId":"0x..."                    //节点id
     *   },
     *   "type": "1001"                          //交易类型
     *}
     *
     */
    @PostMapping("transaction/list")
    public List<Transaction> transactionList(@Valid @RequestBody TransactionListReq req){
        return apiService.transactionList(req);
    }
    
    /**
     * @api {post} transaction/listVote b.获取投票交易列表通过节点和地址
     * @apiVersion 1.0.0
     * @apiName transaction/listVote
     * @apiGroup transaction
     * @apiDescription 获取投票交易列表通过节点和地址
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"",                            //链ID (必填)
     *      "beginSequence":120,                 //起始序号 (必填)
     *      "listSize":100,                      //列表大小 (必填)
     *      "nodeId":"0x",                       //节点ID
     *      "walletAddrs":[                      //地址列表
     *         "address1",
     *         "address2" 
     *      ]
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg":"",                         //描述信息
     *      "code":0,                            //成功（0），失败则由相关失败码
     *      "data":[
     *           {
     *              "nodeId":"0x",               //节点ID
     *              "name":"node-1",             //节点名称
     *              "validNum":"50",             //有效票
     *              "totalTicketNum":"100",      //总票数
     *              "locked":"",                 //投票锁定,单位Energon
     *              "earnings":"",               //投票收益,单位Energon
     *              "transactiontime":""         //最新投票时间，单位-毫秒
     *              "deposit":"1000000000000",   //当时的购票价格，单位Energon
     *              "owner":"0x..."              //投票人钱包地址
     *           }
     *       ]
     * }
     */
    @PostMapping("transaction/listVote")
    public List<Transaction> transactionList14(){
        return null;
    }
}
