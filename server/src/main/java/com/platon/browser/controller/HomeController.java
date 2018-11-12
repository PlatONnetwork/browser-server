package com.platon.browser.controller;

import com.alibaba.fastjson.JSON;
import com.platon.browser.common.base.BaseResp;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dto.*;
import com.platon.browser.dto.cache.BlockInit;
import com.platon.browser.dto.cache.TransactionInit;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.query.Query;
import com.platon.browser.service.CacheService;
import com.platon.browser.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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
    private ChainsConfig chainsConfig;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private SearchService searchService;
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
    public BaseResp nodeInit(@DestinationVariable String chainId) {
        logger.debug("获取节点初始化列表数据！");
        if(!chainsConfig.isValid(chainId)){
            return BaseResp.build(RetEnum.RET_PARAM_VALLID.getCode(),"链ID错误！",null);
        }
        List<NodeInfo> nodeInfoList = cacheService.getNodeInfoList(chainId);
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
    public BaseResp indexInit(@DestinationVariable String chainId) {
        logger.debug("获取节点初始化列表数据！");
        if(!chainsConfig.isValid(chainId)){
            return BaseResp.build(RetEnum.RET_PARAM_VALLID.getCode(),"链ID错误！",null);
        }
        IndexInfo indexInfo = cacheService.getIndexInfo(chainId);
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
    public BaseResp statisticInit(@DestinationVariable String chainId) {
        logger.debug("获取出块时间及交易数据初始数据！");
        if(!chainsConfig.isValid(chainId)){
            return BaseResp.build(RetEnum.RET_PARAM_VALLID.getCode(),"链ID错误！",null);
        }
        StatisticInfo statistic = cacheService.getStatisticInfo(chainId);
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
    public BaseResp blockInit(@DestinationVariable String chainId) {
        logger.debug("获取区块列表初始数据！");
        if(!chainsConfig.isValid(chainId)){
            return BaseResp.build(RetEnum.RET_PARAM_VALLID.getCode(),"链ID错误！",null);
        }
        BlockInit blockInit = cacheService.getBlockInit(chainId);
        BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),blockInit.getList());
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
     *              "txType": "", // 交易类型
                        transfer ：转账
                        MPCtransaction ： MPC交易
                        contractCreate ： 合约创建
                        vote ： 投票
                        transactionExecute ： 合约执行
                        authorization ： 权限
     *      	    "value": 3.6,//数额
     *      	    "timestamp"：155788//交易时间
     *           }
     *      ]
     *   }
     */
    @SubscribeMapping("/transaction/init?cid={chainId}")
    public BaseResp transactionInit(@DestinationVariable String chainId) {
        logger.debug("获取出块时间及交易数据初始数据！");
        if(!chainsConfig.isValid(chainId)){
            return BaseResp.build(RetEnum.RET_PARAM_VALLID.getCode(),"链ID错误！",null);
        }
        TransactionInit transactionInit = cacheService.getTransactionInit(chainId);
        BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),transactionInit.getList());
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
     *          "type":"",//区块block，交易transaction，节点node,合约contract,账户account,挂起交易pending
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
    public BaseResp search(@Valid @RequestBody SearchParam param){
        try{
            logger.debug(JSON.toJSONString(param));
            Query query = searchService.findInfoByParam(param);
            BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),RetEnum.RET_SUCCESS.getName(),query);
            return resp;
        }catch (BusinessException be){
            return BaseResp.build(be.getErrorCode(),be.getErrorMessage(),null);
        }
    }
}