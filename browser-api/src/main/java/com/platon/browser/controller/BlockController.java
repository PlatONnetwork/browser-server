package com.platon.browser.controller;

import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockDetail;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.exception.BusinessException;
import com.platon.browser.exception.ResponseException;
import com.platon.browser.req.block.BlockDetailNavigateReq;
import com.platon.browser.req.block.BlockDetailReq;
import com.platon.browser.req.block.BlockPageReq;
import com.platon.browser.req.block.BlockTransactionPageReq;
import com.platon.browser.res.BaseResp;
import com.platon.browser.service.BlockService;
import com.platon.browser.service.RedisCacheService;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * User: dongqile
 * Date: 2018/10/23
 * Time: 09:35
 */
@RestController
@RequestMapping("/block")
public class BlockController  {

    @Autowired
    private I18nUtil i18n;
    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private BlockService blockService;
    @Autowired
    private RedisCacheService redisCacheService;
    private static Logger logger = LoggerFactory.getLogger(BlockController.class);

    /**
     *
     * @api {post} block/blockList a.区块列表
     *       @apiVersion 1.0.0
     *       @apiName blockList
     *       @apiGroup block
     *       @apiDescription 区块列表
     *       @apiParamExample {json} Request-Example:
     *       {
     *            "cid":"", // 链ID (必填)
     *            "pageNo": 1,//页数(必填)
     *            "pageSize": 10,//页大小(必填)
     *       }
     *
     *       @apiSuccessExample {json} Success-Response:
     *       HTTP/1.1 200 OK
     *       {
     *            "errMsg": "",//描述信息
     *            "code": 0,//成功（0），失败则由相关失败码
     *            "displayTotalCount":18,//显示总数
     *            "totalCount":18,// 小于等于500000记录的总数
     *            "totalPages":1,//总页数
     *            "data": [
     *                        {
     *                        "height": 17888,//块高
     *                        "timestamp": 1798798798798,//出块时间
     *                        "transaction": 10000,//块内交易数
     *                        "size": 188,//块大小
     *                        "miner": "0x234", // 出块节点地址
     *                        "nodeName": "node-01", // 出块节点名称
     *                        "energonUsed": 111,//能量消耗
     *                        "energonLimit": 24234,//能量消耗限制
     *                        "energonAverage": 11, //平均能量价值(单位:Energon)
     *                        "blockReward": "123123",//区块奖励(单位:Energon)
     *                        "serverTime": 1708098077,  //服务器时间
     *                        "blockVoteAmount":,//区块内投票交易个数
     *                        "blockVoteNumber":,//区块中包含的选票张数
     *                        "blockCampaignAmount"://区块内竞选交易个数
     *                        }
     *                    ]
     *       }
     * */
    @PostMapping("blockList")
    public RespPage<BlockListItem> blockList (@Valid @RequestBody BlockPageReq req) {
        if(!chainsConfig.isValid(req.getCid())){
            throw new ResponseException(i18n.i(I18nEnum.CHAIN_ID_ERROR,req.getCid()));
        }
        RespPage<BlockListItem> page = redisCacheService.getBlockPage(req.getCid(),req.getPageNo(),req.getPageSize());
        return page;
    }

    /**
     * @api {post} block/blockDetails b.区块详情
     * @apiVersion 1.0.0
     * @apiName blockDetails
     * @apiGroup block
     * @apiDescription 区块详情
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "height": 123,//区块高度(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": {
     *           "height": 19988,//块高
     *           "timestamp": 123123123879,//出块时间
     *           "hash": "0x1238",//区块hash
     *           "parentHash": "0x234",//父区块hash
     *           "miner": "0x234", // 出块节点地址
     *           "nodeName": "node-01", // 出块节点名称
     *           "timeDiff":424234, // 当前块出块时间距离上一个块出块时间之差（毫秒）
     *           "size": 123,//区块大小
     *           "energonLimit": 24234,//能量消耗限制
     *           "energonUsed": 2342,//能量消耗
     *           "blockReward": "123123",//区块奖励(单位:Energon)
     *           "extraData": "xxx",//附加数据
     *           "first":false, // 是否第一条记录
     *           "last":true // 是否最后一条记录
     *           "transaction": 1288,//块内交易总数
     *           "blockVoteAmount":,//区块内投票交易个数
*                "blockVoteNumber":,//区块中包含的选票张数
*                "blockCampaignAmount"://区块内竞选交易个数
     *           }
     * }
     */
    @PostMapping("blockDetails")
    public BaseResp blockDetails (@Valid @RequestBody BlockDetailReq req) {
        if(!chainsConfig.isValid(req.getCid())){
            throw new ResponseException(i18n.i(I18nEnum.CHAIN_ID_ERROR,req.getCid()));
        }
        try{
            BlockDetail blockDetail = blockService.getDetail(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),blockDetail);
        }catch (BusinessException be){
            throw new ResponseException(be.getMessage());
        }
    }


    /**
     * @api {post} block/blockDetailNavigate c.区块详情前后跳转浏览
     * @apiVersion 1.0.0
     * @apiName blockDetailNavigate
     * @apiGroup block
     * @apiDescription 区块详情前后跳转浏览
     * @apiParamExample {json} Request-Example:
     * {
     *      "cid":"", // 链ID (必填)
     *      "direction":"", // 方向：prev-上一个，next-下一个 (必填)
     *      "height": 123,//区块高度(必填)
     * }
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": {
     *           "height": 19988,//块高
     *           "timestamp": 123123123879,//出块时间
     *           "transaction": 1288,//块内交易总数
     *           "hash": "0x1238",//区块hash
     *           "parentHash": "0x234",//父区块hash
     *           "miner": "0x234", // 出块节点地址
     *           "nodeName": "node-01", // 出块节点名称
     *           "size": 123,//区块大小
     *           "energonLimit": 24234,//能量消耗限制
     *           "energonUsed": 2342,//能量消耗
     *           "blockReward": "123123",//区块奖励(单位:Energon)
     *           "extraData": "xxx",//附加数据
     *           "first":false, // 是否第一条记录
     *           "last":true // 是否最后一条记录
     *      }
     * }
     */
    @PostMapping("blockDetailNavigate")
    public BaseResp blockDetailNavigate (@Valid @RequestBody BlockDetailNavigateReq req) {
        if(!chainsConfig.isValid(req.getCid())){
            throw new ResponseException(i18n.i(I18nEnum.CHAIN_ID_ERROR,req.getCid()));
        }
        try{
            BlockDetail blockDetail = blockService.getDetailNavigate(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),blockDetail);
        }catch (BusinessException be){
            throw new ResponseException(be.getMessage());
        }
    }

    /**
     * @api {post} block/transactionList d.区块交易列表
     * @apiVersion 1.0.0
     * @apiName transactionList
     * @apiGroup block
     * @apiDescription 区块交易列表
     * @apiParamExample {json} Request-Example:
     * {
     * "cid":"", // 链ID (必填)
     * "pageNo": 1,//页数(必填)
     * "pageSize": 10,//页大小(必填)
     * "blockNumber":500, // 区块号(必填)
     * "txType":"", // 交易类型 (可选), 可以设置多个类型，使用逗号“,”分隔
     *     transfer ：转账
     *     MPCtransaction ： MPC交易
     *     contractCreate ： 合约创建
     *     voteTicket ： 投票
     *     transactionExecute ： 合约执行
     *     authorization ： 权限
     *     candidateDeposit ： 竞选质押
     *     candidateApplyWithdraw ： 减持质押
     *     candidateWithdraw ： 提取质押
     *     unknown ： 未知
     * }
*
     * @apiSuccessExample {json} Success-Response:
     * HTTP/1.1 200 OK
     * {
     * "errMsg": "",//描述信息
     * "code": 0,//成功（0），失败则由相关失败码
     * "displayTotalCount":18,//显示总数
     * "totalCount":18,// 小于等于500000记录的总数
     * "totalPages":1,//总页数
     * "data": [
     *     {
     *     "txHash": "0x234234",//交易hash
     *     "blockHeight": "15566",//交易所在区块高度
     *     "blockTime": 18080899999,//出块时间
     *     "from": "0x667766",//发送方, 必定是钱包地址
     *     "to": "0x667766",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：
     *     // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址
     *     "value": "222",//数额(单位:Energon)
     *     "actualTxCost": "22",//交易费用(单位:Energon)
     *     "txReceiptStatus": 1,//交易状态 -1 pending 1 成功  0 失败
     *     "txType": "", // 交易类型
     *         transfer ：转账
     *         MPCtransaction ： MPC交易
     *         contractCreate ： 合约创建
     *         voteTicket ： 投票
     *         transactionExecute ： 合约执行
     *         authorization ： 权限
     *         candidateDeposit ： 竞选质押
     *         candidateApplyWithdraw ： 减持质押
     *         candidateWithdraw ： 提取质押
     *         unknown ： 未知
     *     "txInfo": "{
     *         "functionName":"",//方法名称
     *         "parameters":{},//参数
     *         "type":"1"//交易类型
     *             0：转账
     *             1：合约发布
     *             2：合约调用
     *             4：权限
     *             5：MPC交易
     *             1000：投票
     *             1001：竞选质押
     *             1002：减持质押
     *             1003：提取质押
     *     }"//返回交易解析结构
     *
     *     "serverTime": 1123123,//服务器时间
     *     "failReason":"",//失败原因
     *     "receiveType":"account" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址，
     *     // 前端页面在点击接收方的地址时，根据此字段来决定是跳转到账户详情还是合约详情
     *   }
     * ]
     **/
    @PostMapping("transactionList")
    public RespPage<TransactionListItem> transactionList (@Valid @RequestBody BlockTransactionPageReq req) {
        if(!chainsConfig.isValid(req.getCid())){
            throw new ResponseException(i18n.i(I18nEnum.CHAIN_ID_ERROR,req.getCid()));
        }
        RespPage<TransactionListItem> page = blockService.getBlockTransactionList(req);
        return page;
    }
}