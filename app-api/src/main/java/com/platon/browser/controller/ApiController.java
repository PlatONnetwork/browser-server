package com.platon.browser.controller;

import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.transaction.TransactionVoteReq;
import com.platon.browser.dto.transaction.VoteInfo;
import com.platon.browser.dto.transaction.VoteSummary;
import com.platon.browser.dto.transaction.VoteTransaction;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.ResponseException;
import com.platon.browser.req.transaction.CandidateTicketCountReq;
import com.platon.browser.req.transaction.TicketCountByTxHashReq;
import com.platon.browser.req.transaction.TransactionListReq;
import com.platon.browser.req.transaction.VoteSummaryReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.service.ApiService;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * User: dongqile
 * Date: 2019/3/19
 * Time: 11:39
 */
@RestController
@RequestMapping("/api")
public class ApiController {

    private static Logger logger = LoggerFactory.getLogger(ApiController.class);
    @Autowired
    private I18nUtil i18n;
    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private ApiService apiService;

    /**
     * @api {post} api/getBatchVoteSummary a.获取统计统计信息
     * @apiVersion 1.0.0
     * @apiName GetBatchVoteSummary
     * @apiGroup api
     * @apiDescription 获取统计统计信息
     * @apiParamExample {json} Request-Example:
     * {
     *     "cid":"", // 链ID (必填)
     *      "addressList":[
     *       addlist1,      //地址列表list<String>
     *       addlist2
     *       ]
     * }
     * @apiSuccessExample {json} Success-Response:
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": [
     *               {
     *               "locked":"1999900000",//String类型,投票锁定,单位Energon
     *               "earnings":"1999900000",//String类型,投票收益,单位Energon
     *               "totalTicketNum": "100",//string！！！ 总票数
     *               "validNum":"2"//String类型,有效票数
     *               }
     *       ]
     * }
     */

    @PostMapping("getBatchVoteSummary")
    public RespPage<VoteSummary> getBatchVoteSummary(@Valid @RequestBody VoteSummaryReq req){
        if(req.getAddressList().size() < 0){
            throw new ResponseException(i18n.i(I18nEnum.FAILURE));
        }
        List<VoteSummary> res = apiService.getVoteSummary(req.getAddressList(),req.getCid());
        RespPage<VoteSummary> list = new RespPage<>();
        list.setData(res);
        return list;
    }


    /**
     * @api {post} api/getBatchVoteTransaction b.批量获选票交易
     * @apiVersion 1.0.0
     * @apiName getBatchVoteTransaction
     * @apiGroup api
     * @apiDescription 批量获选票交易
     * @apiParamExample {json} Request-Example:
     * {
     *      "pageNo": 1,//页数(必填)默认1
     *      "pageSize": 10,//页大小(必填)默认10,
     *        "cid":"", // 链ID (必填)
     *       "walletAddrs" :
     *          [
     *          addlist1,      //地址列表list<String>
     *          addlist2
     *          ]
     * }
     * @apiSuccessExample {json} Success-Response:
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "totalCount":18,//总数
     *      "totalPages":1,//总页数
     *      "data": [
     *           {
     *           "TransactionHash":"0xbbbbbb...",//String类型
     *           "candidateId":"0xffffff....",//候选人Id
     *           "owner":"0xbbb..."//投票人钱包地址，
     *           "earnings":"90000000000000000",//此次交易投票获得的收益，单位Energon
     *           "transactiontime":"1231231233".//Unix时间戳，毫秒级,交易时间
     *           "deposit":"1000000000000",// 当时的购票价格，单位Energon
     *           "totalTicketNum": "100",// 总票数
     *           "validNum": "50",// 有效票
     *       }
     *       ]
     * }
     */
    @PostMapping("getBatchVoteTransaction")
    public RespPage<VoteTransaction> getBatchVoteTransaction(@Valid @RequestBody TransactionVoteReq req){
        RespPage<VoteTransaction> res = apiService.getVoteTransaction(req);
        return res;
    }

    /**
     * @api {post} api/getCandidateTicketCount c.批量获取节点有效选票
     * @apiVersion 1.0.0
     * @apiName getCandidateTicketCount
     * @apiGroup api
     * @apiDescription 批量获取节点有效选票
     * @apiParamExample {json} Request-Example:
     * {
     *
     *       "nodeIds" :
     *          [
     *          nodeId1,      //节点id列表list<String>
     *          nodeId2
     *          ],
     *         "cid":"", // 链ID (必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": [
     *           {
     *           "nodeId1" : 10, //节点id : 有效票
     *           "nodeId2" : 1,
     *           "nodeId3" : 3
     *       }
     *       ]
     * }
     */
    @PostMapping("getCandidateTicketCount")
    public BaseResp getCandidateTicketCount( @Valid @RequestBody CandidateTicketCountReq req){
        Map<String,Integer> map = apiService.getCandidateTicketCount(req.getNodeIds(), req.getCid());
        return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),map);
    }



    /**
     * @api {post} api/getTicketCountByTxHash d.批量获取投票交易相关信息
     * @apiVersion 1.0.0
     * @apiName getTicketCountByTxHash
     * @apiGroup api
     * @apiDescription 批量获取投票交易相关信息
     * @apiParamExample {json} Request-Example:
     * {
     *       "hashList" :
     *          [
     *          "0xqew1652131d3...."      //hash列表list<String>
     *          ]
     *       "cid":"", // 链ID (必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "totalCount":18,//总数
     *      "totalPages":1,//总页数
     *      "data": [
     *           {
     *           "nodeId":"0xbbbbbb...",//节点Id
     *           "nodeName":"123",//节点名称
     *           "vailVoteCount":12,//有效票数
     *           "voteSum":22,//总票数
     *           "deadLine":"1231231233".//实际过期时间
     *           "walletAddress":"0xbbbbbb",//钱包地址
     *           "price":"100"//票价
     *           "income":,//收益
     *       }
     *       ]
     * }
     */
    @PostMapping("getTicketCountByTxHash")
    public RespPage<VoteInfo> getTicketCountByTxHash(@Valid @RequestBody TicketCountByTxHashReq req){
        return apiService.getTicketCountByTxHash(req);
    }

    /**
     * @api {post} api/transaction/list e.通过地址和指定交易序号查询交易列表
     * @apiVersion 1.0.0
     * @apiName transaction/list
     * @apiGroup api
     * @apiDescription 通过地址和指定交易序号查询交易列表
     * @apiParamExample {json} Request-Example:
     * {
     *       "address":"0xsdfsdfsdfsdf", // 地址 (必填)
     *       "beginSequence":120, // 起始序号 (必填)
     *       "listSize":100, // 列表大小 (必填)
     *       "cid":"", // 链ID (必填)
     * }
     * @apiSuccessExample {json} Success-Response:
        [
            {
            "actualTxCost":"21168000000000", // 交易实际花费值(手续费)，单位：wei
            "blockHash":"0x985447eb289ca2e277b62356252504ea63ecb7a167d641321a64179d4c7ef797", // 区块hash
            "blockNumber":187566, // 区块高度
            "chainId":"203", // 链id
            "createTime":1557484976000, //  创建时间
            "energonLimit":"210000", // 能量限制
            "energonPrice":"1000000000", // 能量价格
            "energonUsed":"21168", // 能量消耗
            "from":"0xbae514b5f89a90e16535c87bcc72ea0619046a62", // 交易发起方地址
            "hash":"0x9dd74d1bb44afc8b5be8c21263824ea4acdc7e153b4e6bac3691ce9186500b8c", // 交易hash
            "nonce":"10", // Nonce值
            "receiveType":"account", // 交易接收者类型（to是合约还是账户）contract合约、 account账户
            "sequence":153, // 排列序号：由区块号和交易索引拼接而成
            "timestamp":1557484976000, // 交易时间（单位：毫秒）
            "to":"0x72adbbfd846f34ff54456219ef750e53621b6cc1", // 交易接收方地址
            "transactionIndex":0, // 交易在区块中位置
            "txInfo":"{\"parameters\":{},\"type\":\"0\"}", // 交易信息
            "txReceiptStatus":1, // 交易状态 1 成功 0 失败
            "txType":"transfer", // 交易类型 transfer ：转账 MPCtransaction ： MPC交易 contractCreate ： 合约创建 vote ： 投票 transactionExecute ： 合约执行 authorization ： 权限 candidateDeposit：竞选质押 candidateApplyWithdraw：减持质押 candidateWithdraw：提取质押 unknown：未知
            "updateTime":1557484976000, // 更新时间
            "value":"1000000000000000000" // 交易金额
            }
        ]
     */
    @PostMapping("transaction/list")
    public List<Transaction> transactionList(@Valid @RequestBody TransactionListReq req){
        return apiService.transactionList(req);
    }

    /**
     * @api {post} api/node/list f.获取节点列表
     * @apiVersion 1.0.0
     * @apiName node/list
     * @apiGroup api
     * @apiDescription 获取节点列表
     * @apiParamExample {json} Request-Example:
     * {
     *       "cid":"",                        //链ID (必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg":"",                      //描述信息
     *      "code":0,                         //成功（0），失败则由相关失败码
     *      "voteCount":90,                   //已投票
     *      "proportion":86,                  //投票率:小数
     *      "ticketPrice":3.66,               //票价（单位Energon）
     *      "data":[
     *           {
     *           "nodeId":"0x",               //节点ID
     *           "ranking":1,                 //质押排名
     *           "name":"node-1",             //节点名称
     *           "countryCode":"CN",          //国家代码
     *           "countryEnName":"",          //国家英文名称
     *           "countryCnName":"",          //国家中文名称
     *           "countrySpellName":"",       //国家拼音名称，中文环境下，区域进行排序
     *           "deposit":"1.254555555",     //质押金(单位:Energon)
     *           "rewardRatio":0.02           //投票激励:小数
     *           }
     *       ]
     * }
     */
    @PostMapping("transaction/list11")
    public List<Transaction> transactionList11(){
        return null;
    }
    
    /**
     * @api {post} api/node/details g.获取节点详情
     * @apiVersion 1.0.0
     * @apiName node/details
     * @apiGroup api
     * @apiDescription 获取节点详情
     * @apiParamExample {json} Request-Example:
     * {
     *       "cid":"",                        //链ID (必填)
     *       "nodeId":"0x",                   //节点ID
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg":"",                      //描述信息
     *      "code":0,                         //成功（0），失败则由相关失败码
     *      "data": {
     *           "nodeId":"0x",               //节点ID
     *           "ranking":1,                 //质押排名
     *           "name":"node-1",             //节点名称
     *           "deposit":"1.254555555",     //质押金(单位:Energon)
     *           "rewardRatio":0.02           //投票激励:小数
     *           "orgName":"",                //机构名称
     *           "orgWebsite":"",             //机构官网
     *           "intro":"",                  //节点简介
     *           "nodeUrl":"",                //节点地址
     *           "ticketCount":"",            //得票数
     *           "joinTime":199880011,        //加入时间，单位-毫秒
     *           "electionStatus":1,          //竞选状态:1-候选前100名,2-出块中,3-验证节点,4-备选前100名 ？
     *      }
     * }
     */
    @PostMapping("transaction/list12")
    public List<Transaction> transactionList12(){
        return null;
    }
    
    /**
     * @api {post} api/vote/groupByNode h.获取投票列表
     * @apiVersion 1.0.0
     * @apiName vote/groupByNode
     * @apiGroup api
     * @apiDescription 获取投票列表
     * @apiParamExample {json} Request-Example:
     * {
     *       "cid":"",                        //链ID (必填)
     *       "walletAddrs":[                  //地址列表
     *          "address1",
     *          "address2" 
     *       ]
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg":"",                      //描述信息
     *      "code":0,                         //成功（0），失败则由相关失败码
     *      "data":[
     *           {
     *           "nodeId":"0x",               //节点ID
     *           "name":"node-1",             //节点名称
     *           "countryCode":"CN",          //国家代码
     *           "countryEnName":"",          //国家英文名称
     *           "countryCnName":"",          //国家中文名称
     *           "validNum":"50",             //有效票
     *           "totalTicketNum":"100",      //总票数
     *           "locked":"",                 //投票锁定,单位Energon
     *           "earnings":"",               //投票收益,单位Energon
     *           "transactiontime":""         //最新投票时间，单位-毫秒
     *           }
     *       ]
     * }
     */
    @PostMapping("transaction/list13")
    public List<Transaction> transactionList13(){
        return null;
    }
    
    /**
     * @api {post} api/vote/listTxByNodeAndAdress i.获取投票交易列表通过节点和地址
     * @apiVersion 1.0.0
     * @apiName vote/listTxByNodeAndAdress
     * @apiGroup api
     * @apiDescription 获取投票列表
     * @apiParamExample {json} Request-Example:
     * {
     *       "cid":"",                        //链ID (必填)
     *       "nodeId":"0x",                   //节点ID
     *       "walletAddrs":[                  //地址列表
     *          "address1",
     *          "address2" 
     *       ],
     *       "beginSequence":120,             //起始序号 (必填)
     *       "listSize":100,                  //列表大小 (必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg":"",                      //描述信息
     *      "code":0,                         //成功（0），失败则由相关失败码
     *      "data":[
     *           {
     *           "nodeId":"0x",               //节点ID
     *           "name":"node-1",             //节点名称
     *           "validNum":"50",             //有效票
     *           "totalTicketNum":"100",      //总票数
     *           "locked":"",                 //投票锁定,单位Energon
     *           "earnings":"",               //投票收益,单位Energon
     *           "transactiontime":""         //最新投票时间，单位-毫秒
     *           "deposit":"1000000000000",   //当时的购票价格，单位Energon
     *           "owner":"0x..."              //投票人钱包地址
     *           }
     *       ]
     * }
     */
    @PostMapping("transaction/list14")
    public List<Transaction> transactionList14(){
        return null;
    }
}
