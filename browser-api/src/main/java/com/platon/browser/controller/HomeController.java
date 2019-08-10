package com.platon.browser.controller;

import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.IndexInfo;
import com.platon.browser.dto.StatisticInfo;
import com.platon.browser.dto.block.BlockPushItem;
import com.platon.browser.dto.transaction.TransactionPushItem;
import com.platon.browser.enums.RetEnum;
import com.platon.browser.res.BaseResp;
import com.platon.browser.service.NodeService;
import com.platon.browser.service.StatisticService;
import com.platon.browser.enums.I18nEnum;
import com.platon.browser.service.cache.BlockCacheService;
import com.platon.browser.service.cache.TransactionCacheService;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private I18nUtil i18n;
    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private TransactionCacheService transactionCacheService;
    @Autowired
    private StatisticService statisticService;

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
     *      	    "netState": ,//节点状态 1 正常 2 异常
     *      	    "nodeName": //节点名称
     *           }
     *      ]
     *   }
     */
    @SubscribeMapping("/node/init?cid={chainId}")
    public BaseResp nodeInit(@DestinationVariable String chainId) {
        logger.debug("获取节点初始化列表数据！");
        if(!chainsConfig.isValid(chainId)){
            return BaseResp.build(RetEnum.RET_PARAM_VALLID.getCode(),i18n.i(I18nEnum.CHAIN_ID_ERROR,chainId),null);
        }
        BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),"");
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
     *      	    "miner":"",//出块节点地址
     *      	    "nodeName":"",//出块节点名称
     *      	    "currentTransaction": //当前交易笔数
     *      	    "consensusNodeAmount": //竞选节点数
     *      	    "addressAmount": //地址数
     *      	    "voteCount": //投票数
     *      	    "proportion": //占比 (小数)
     *      	    "ticketPrice": //票价 (单位: Energon)
     *           }
     *   }
     */
    @SubscribeMapping("/index/init?cid={chainId}")
    public BaseResp indexInit(@DestinationVariable String chainId) {
        logger.debug("获取节点初始化列表数据！");
        if(!chainsConfig.isValid(chainId)){
            return BaseResp.build(RetEnum.RET_PARAM_VALLID.getCode(),i18n.i(I18nEnum.CHAIN_ID_ERROR,chainId),null);
        }
        IndexInfo index = statisticService.getIndexInfo(chainId);
        BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),index);
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
     *      	    "graphData": {
         *    	        "x":[] ,//区块高度
         *    	        "ya":[], //出块的时间
         *    	        "yb":[]  //交易数量数量
         *    	    }
     *           }
     *
     *   }
     */
    @SubscribeMapping("/statistic/init?cid={chainId}")
    public BaseResp statisticInit(@DestinationVariable String chainId) {
        logger.debug("获取出块时间及交易数据初始数据！");
        if(!chainsConfig.isValid(chainId)){
            return BaseResp.build(RetEnum.RET_PARAM_VALLID.getCode(),i18n.i(I18nEnum.CHAIN_ID_ERROR,chainId),null);
        }
        StatisticInfo statistic = statisticService.getStatisticInfo(chainId);
        BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),statistic);
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
            return BaseResp.build(RetEnum.RET_PARAM_VALLID.getCode(),i18n.i(I18nEnum.CHAIN_ID_ERROR,chainId),null);
        }
        BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),"");
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
     *      	    "to": "0x667766",//接收方, 此字段存储的可能是钱包地址，也可能是合约地址，需要使用receiveType来进一步区分：
     *                               // 如果receiveType的值为account，则是钱包地址；如果receiveType的值为contract，则是合约地址
     *              "txType": "", // 交易类型
                        transfer ：转账
                        MPCtransaction ： MPC交易
                        contractCreate ： 合约创建
                        vote ： 投票
                        transactionExecute ： 合约执行
                        authorization ： 权限
                        candidateDeposit ： 竞选质押
                        candidateApplyWithdraw ： 减持质押
                        candidateWithdraw ： 提取质押
                        unknown ： 未知
     *      	    "value": 3.6,//数额
     *      	    "timestamp"：155788,//交易时间
     *      	    "receiveType":"account" // 此字段表示的是to字段存储的账户类型：account-钱包地址，contract-合约地址
     *           }
     *      ]
     *   }
     */
    @SubscribeMapping("/transaction/init?cid={chainId}")
    public BaseResp transactionInit(@DestinationVariable String chainId) {
        logger.debug("获取出块时间及交易数据初始数据！");
        if(!chainsConfig.isValid(chainId)){
            return BaseResp.build(RetEnum.RET_PARAM_VALLID.getCode(),i18n.i(I18nEnum.CHAIN_ID_ERROR,chainId),null);
        }
        List<TransactionPushItem> transactions = transactionCacheService.getTransactionPushCache(chainId,1,10);
        BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),transactions);
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

}
