package com.platon.browser.controller;

import com.alibaba.fastjson.JSON;
import com.platon.browser.common.base.BaseResp;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.dto.IndexInfo;
import com.platon.browser.dto.SearchParam;
import com.platon.browser.dto.SearchResult;
import com.platon.browser.dto.StatisticInfo;
import com.platon.browser.dto.block.BlockInfo;
import com.platon.browser.dto.block.BlockStatistic;
import com.platon.browser.dto.node.NodeDetail;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.transaction.TransactionInfo;
import com.platon.browser.service.CacheService;
import com.platon.browser.service.NodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * User: dongqile
 * Date: 2018/10/23
 * Time: 9:40
 */
@RestController
public class HomeController {

    private static Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private CacheService cacheService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * @api {subscribe} /app/node/init?cid=:chainId a.节点监控图标数据（websocket请求）初始数据
     * @apiVersion 1.0.0
     * @apiName node/init
     * @apiGroup home
     * @apiDescription 初始数据
     * @apiParam {String} cid 链ID.
     * @apiParamExample {json} Request-Example:
     *   {}
     *  @apiSuccessExample {json} Success-Response:
     *   HTTP/1.1 200 OK
     *   {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data":[
     *           {
     *      	    "longitude": "",//经度
     *      	    "latitude":"",//纬度
     *      	    "nodeType": ,//节点状态：1-共识节点 2-非共识
     *      	    "netState": //节点状态 1 正常 2 异常
     *           }
     *      ]
     *   }
     */
    @SubscribeMapping("/node/init?cid={chainId}")
    public BaseResp nodeInit(@DestinationVariable String chainId, StompHeaderAccessor headerAccessor) {
        Object headers = headerAccessor.getHeader("nativeHeaders");
        logger.debug("获取节点初始化列表数据！");
        List<NodeInfo> nodeInfoList = cacheService.getNodeInfoList();
        BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),nodeInfoList);
        return resp;
    }

    /**
     * @api {subscribe} /topic/node/new?cid=:chainId b.节点监控图标数据（websocket请求）增量数据
     * @apiVersion 1.0.0
     * @apiName node/new
     * @apiGroup home
     * @apiDescription 增量数据
     * @apiParam {String} cid 链ID.
     * @apiParamExample {json} Request-Example:
     *   {}
     * @apiSuccessExample  Success-Response:
     *   HTTP/1.1 200 OK
     */



    /**
     * @api {subscribe} /app/index/init?cid=:chainId c.实时监控指标（websocket请求）初始数据
     * @apiVersion 1.0.0
     * @apiName index/init
     * @apiGroup home
     * @apiDescription 初始数据
     * @apiParam {String} cid 链ID.
     * @apiParamExample {json} Request-Example:
     *   {}
     *  @apiSuccessExample {json} Success-Response:
     *   HTTP/1.1 200 OK
     *   {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": {
     *      	    "currentHeight": ,//当前区块高度
     *      	    "node":"",//出块节点
     *      	    "currentTransaction": //当前交易笔数
     *      	    "consensusNodeAmount": //共识节点数
     *      	    "addressAmount": //地址数
     *      	    "voteAmount": //投票数
     *      	    "proportion": //占比
     *      	    "ticketPrice": //票价
     *           }
     *   }
     */
    @SubscribeMapping("/index/init?cid={chainId}")
    public BaseResp indexInit(@DestinationVariable String chainId, StompHeaderAccessor headerAccessor) {
        Object headers = headerAccessor.getHeader("nativeHeaders");
        logger.debug("获取节点初始化列表数据！");
        IndexInfo indexInfo = cacheService.getIndexInfo();
        BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),indexInfo);
        return resp;
    }


    /**
     * @api {subscribe} /topic/index/new?cid=:chainId d.实时监控指标（websocket请求）增量数据
     * @apiVersion 1.0.0
     * @apiName index/new
     * @apiGroup home
     * @apiDescription 增量数据
     * @apiParam {String} cid 链ID.
     * @apiSuccessExample  Success-Response:
     *   HTTP/1.1 200 OK
     */
    @Scheduled(fixedRate = 1000)
    public void indexSubscribe() throws Exception {
        IndexInfo index = new IndexInfo();
        index.setAddressAmount(3);
        index.setConsensusNodeAmount(33);
        index.setCurrentHeight(333);
        index.setCurrentTransaction(333);
        index.setNode("node-1");
        index.setProportion(66);
        index.setTicketPrice(449);
        index.setVoteAmount(333);
        BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),index);
        String cid = "666"; // 链的标识，需要从订阅的消息中获取
        messagingTemplate.convertAndSend("/topic/index/new?cid="+cid, resp);
    }


    /**
     * @api {subscribe} /app/statistic/init?cid=:chainId e.出块时间及交易数据（websocket请求）初始数据
     * @apiVersion 1.0.0
     * @apiName statistic/init
     * @apiGroup home
     * @apiDescription 初始数据
     * @apiParam {String} cid 链ID.
     * @apiParamExample {json} Request-Example:
     *   {}
     *  @apiSuccessExample {json} Success-Response:
     *   HTTP/1.1 200 OK
     *   {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data": {
     *      	    "avgTime":365 ,//平均出块时长
     *      	    "current":333,//当前交易数量
     *      	    "maxTps":333, //最大交易TPS
     *      	    "avgTransaction":33, //平均区块交易数
     *      	    "dayTransaction":33, //过去24小时交易笔数
     *      	    "blockStatisticList": [
     *      	    {
     *      	        "height":333 ,//区块高度
     *      	        "time":333, //出块的时间
     *      	        "transaction":33  //区块打包数量
     *      	    }
     *      	    ]//投票数
     *           }
     *
     *   }
     */
    @SubscribeMapping("/statistic/init?cid={chainId}")
    public BaseResp statisticInit(@DestinationVariable String chainId, StompHeaderAccessor headerAccessor) {
        Object headers = headerAccessor.getHeader("nativeHeaders");
        logger.debug("获取出块时间及交易数据初始数据！");
        StatisticInfo statistic = cacheService.getStatisticInfo();
        BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),statistic);
        return resp;
    }

    /**
     * @api {subscribe} /topic/statistic/new?cid=:chainId f.出块时间及交易数据（websocket请求）增量数据
     * @apiVersion 1.0.0
     * @apiName statistic/new
     * @apiGroup home
     * @apiDescription 增量数据
     * @apiParam {String} cid 链ID.
     * @apiSuccessExample  Success-Response:
     *   HTTP/1.1 200 OK
     */
    @Scheduled(fixedRate = 1000)
    public void statisticSubscribe() throws Exception {
        StatisticInfo statistic = new StatisticInfo();
        statistic.setAvgTime(333);
        statistic.setAvgTransaction(333);
        statistic.setCurrent(333);
        statistic.setDayTransaction(333);
        statistic.setMaxTps(333);
        List<BlockStatistic> lists = new ArrayList<>();
        BlockStatistic blockStatistic = new BlockStatistic();
        blockStatistic.setHeight(333);
        blockStatistic.setTime(333);
        blockStatistic.setTransaction(333);
        lists.add(blockStatistic);
        statistic.setBlockStatisticList(lists);
        BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),statistic);
        String cid = "666"; // 链的标识，需要从订阅的消息中获取
        messagingTemplate.convertAndSend("/topic/statistic/new?cid="+cid, resp);
    }



    /**
     * @api {subscribe} /app/block/init?cid=:chainId g.实时区块列表（websocket请求）初始数据
     * @apiVersion 1.0.0
     * @apiName block/init
     * @apiGroup home
     * @apiDescription 初始数据
     * @apiParam {String} cid 链ID.
     * @apiParamExample {json} Request-Example:
     *   {}
     *  @apiSuccessExample {json} Success-Response:
     *   HTTP/1.1 200 OK
     *   {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data":[
     *       {
     *      	    "height":33 ,//区块高度
     *      	    "timestamp":33333,//出块时间
     *      	    "serverTime":44444, //服务器时间
     *      	    "node": "node-1",//出块节点
     *      	    "transaction":333, //交易数
     *      	    "blockReward":333 //区块奖励
     *           }
     *      ]
     *   }
     */
    @SubscribeMapping("/block/init?cid={chainId}")
    public BaseResp blockInit(@DestinationVariable String chainId, StompHeaderAccessor headerAccessor) {
        Object headers = headerAccessor.getHeader("nativeHeaders");
        logger.debug("获取出块时间及交易数据初始数据！");
        List<BlockInfo> blockInfos = cacheService.getBlockInfoList();
        BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),blockInfos);
        return resp;
    }


    /**
     * @api {subscribe} /topic/block/new?cid=:chainId h.实时区块列表（websocket请求）增量数据
     * @apiVersion 1.0.0
     * @apiName block/new
     * @apiGroup home
     * @apiDescription 增量数据，可手动开关是否订阅增量数据，开启之后实时接收最新数据推送
     * @apiParam {String} cid 链ID.
     * @apiSuccessExample  Success-Response:
     *   HTTP/1.1 200 OK
     */


    /**
     * @api {subscribe} /app/transaction/init?cid=:chainId i.实时交易列表（websocket请求）初始数据
     * @apiVersion 1.0.0
     * @apiName transaction/init
     * @apiGroup home
     * @apiDescription 初始数据
     * @apiParam {String} cid 链ID.
     * @apiParamExample {json} Request-Example:
     *   {}
     *  @apiSuccessExample {json} Success-Response:
     *   HTTP/1.1 200 OK
     *   {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data":[
     *           {
     *      	    "txHash": "x3222",//交易Hash
     *      	    "blockHeight":5555, // 区块高度
     *      	    "transactionIndex": 33, // 交易在区块中位置
     *      	    "from":"ddddd",//交易发起方地址
     *      	    "to":"aaaa", //交易接收方地址
     *      	    "value": 3.6,//数额
     *      	    "timestamp"：155788//交易时间
     *           }
     *      ]
     *   }
     */
    @SubscribeMapping("/transaction/init?cid={chainId}")
    public BaseResp transactionInit(@DestinationVariable String chainId, StompHeaderAccessor headerAccessor) {
        Object headers = headerAccessor.getHeader("nativeHeaders");
        logger.debug("获取出块时间及交易数据初始数据！");
        List<TransactionInfo> transactionInfos = cacheService.getTransactionInfoList();
        BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),transactionInfos);
        return resp;
    }


    /**
     * @api {subscribe} /topic/transaction/new?cid=:chainId j.实时交易列表（websocket请求）增量数据
     * @apiVersion 1.0.0
     * @apiName transaction/new
     * @apiGroup home
     * @apiDescription 增量数据，可手动开关是否订阅增量数据，开启之后实时接收最新数据推送
     * @apiParam {String} cid 链ID.
     * @apiSuccessExample  Success-Response:
     *   HTTP/1.1 200 OK
     */



    /**
     * @api {post} /home/query k.搜索
     * @apiVersion 1.0.0
     * @apiName query
     * @apiGroup home
     * @apiDescription 根据区块高度，区块hash，交易hash等查询信息
     * @apiParam {String} cid 链ID.
     * @apiParamExample {json} Request-Example:
     *   {
     *       "cid":"", // 链ID (必填)
     *       "parameter":""//块高，块hash，交易hash等
     *   }
     *  @apiSuccessExample {json} Success-Response:
     *   HTTP/1.1 200 OK
     *   {
     *      "errMsg": "",//描述信息
     *      "code": 0,//成功（0），失败则由相关失败码
     *      "data":{
     *          "type":"",//区块block，交易transaction，节点node,合约contract,账户account
     *           "struct":{
     *      	        "height": 17888,//块高
     *                  "timestamp": 1798798798798,//出块时间
     *                  "transaction": 10000,//块内交易数
     *                  "size": 188,//块大小
     *                  "miner": "0x234", // 出块节点
     *                  "energonUsed": 111,//能量消耗
     *                  "energonAverage": 11, //平均能量价值
     *                  "blockReward": "123123",//区块奖励
     *                  "serverTime": 1708098077  //服务器时间
     *           }
     *        }
     *
     *
     *   }
     */


    @PostMapping("/home/query")
    public BaseResp search(@RequestBody SearchParam param){
        logger.debug(JSON.toJSONString(param));
        SearchResult<NodeDetail> searchResult = new SearchResult<>();
        searchResult.setType("block");
        NodeDetail nodeDetail = new NodeDetail();
        nodeDetail.setBlockReward(33.3);
        nodeDetail.setEnergonAverage(333);
        nodeDetail.setEnergonUsed(33);
        nodeDetail.setHeight(333);
        nodeDetail.setMiner("33333");
        nodeDetail.setServerTime(System.currentTimeMillis());
        nodeDetail.setSize(3333);
        nodeDetail.setTimestamp(System.currentTimeMillis());
        nodeDetail.setTransaction(333);
        searchResult.setStruct(nodeDetail);
        BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),searchResult);
        return resp;
    }
}