package com.platon.browser.controller;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.platon.browser.cache.StompCacheInitializer;
import com.platon.browser.common.base.BaseResp;
import com.platon.browser.common.enums.RetEnum;
import com.platon.browser.common.exception.BusinessException;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.IndexInfo;
import com.platon.browser.dto.StatisticGraphData;
import com.platon.browser.dto.StatisticInfo;
import com.platon.browser.dto.StatisticItem;
import com.platon.browser.dto.cache.BlockInit;
import com.platon.browser.dto.cache.LimitQueue;
import com.platon.browser.dto.cache.TransactionInit;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.search.SearchResult;
import com.platon.browser.exception.ResponseException;
import com.platon.browser.req.search.SearchReq;
import com.platon.browser.service.SearchService;
import com.platon.browser.service.StompCacheService;
import com.platon.browser.util.I18nEnum;
import com.platon.browser.util.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private SearchService searchService;
    @Autowired
    private StompCacheService cacheService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private TransactionMapper transactionMapper;

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
            return BaseResp.build(RetEnum.RET_PARAM_VALLID.getCode(),i18n.i(I18nEnum.CHAIN_ID_ERROR,chainId),null);
        }
        Set<NodeInfo> nodeInfoList = cacheService.getNodeInfoSet(chainId);
        BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),nodeInfoList);
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
            return BaseResp.build(RetEnum.RET_PARAM_VALLID.getCode(),i18n.i(I18nEnum.CHAIN_ID_ERROR,chainId),null);
        }
        IndexInfo indexInfo = cacheService.getIndexInfo(chainId);
        BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),indexInfo);
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
        StatisticInfo statistic = cacheService.getStatisticInfo(chainId);


        LimitQueue<StatisticItem> limitQueue = statistic.getLimitQueue();
        List<StatisticItem> itemList = limitQueue.list();
        Collections.sort(itemList,(c1, c2)->{
            // 按区块高度正排
            if(c1.getHeight()>c2.getHeight()) return 1;
            if(c1.getHeight()<c2.getHeight()) return -1;
            return 0;
        });

        StatisticGraphData graphData = new StatisticGraphData();
        for (int i=0;i<itemList.size();i++){
            StatisticItem item = itemList.get(i);
            if(i==0||i==itemList.size()-1) continue;
            StatisticItem prevItem = itemList.get(i-1);
            graphData.getX().add(item.getHeight());
            graphData.getYa().add((item.getTime()-prevItem.getTime())/1000);
            graphData.getYb().add(item.getTransaction()==null?0:item.getTransaction());
        }
        statistic.setGraphData(graphData);


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
        BlockInit blockInit = cacheService.getBlockInit(chainId);
        BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),blockInit.getList());
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
        TransactionInit transactionInit = cacheService.getTransactionInit(chainId);
        BaseResp resp = BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),transactionInit.getList());
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
     * @api {post} /home/search k.搜索
     * @apiVersion 1.0.0
     * @apiName search
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
    public BaseResp search(@Valid @RequestBody SearchReq req){
        if(!chainsConfig.isValid(req.getCid())){
            throw new ResponseException(i18n.i(I18nEnum.CHAIN_ID_ERROR,req.getCid()));
        }
        try{
            SearchResult<?> result = searchService.search(req);
            return BaseResp.build(RetEnum.RET_SUCCESS.getCode(),i18n.i(I18nEnum.SUCCESS),result);
        }catch (BusinessException be){
            throw new ResponseException(be.getErrorMessage());
        }
    }

    @GetMapping("/refreshRedisCache")
    public String refreshRedisCache(){
        chainsConfig.getChainIds().forEach(chainId->{
            String cacheKey = "platon:server:chain_"+chainId+":blocks";;

            for(int i=0;i<500;i++){
                BlockExample condition = new BlockExample();
                condition.createCriteria().andChainIdEqualTo("1");
                condition.setOrderByClause("number desc");

                PageHelper.startPage(i+1,1000);
                List<Block> blockList = blockMapper.selectByExample(condition);
                if(blockList.size()==0) break;

                Set<ZSetOperations.TypedTuple<String>> tupleSet = new HashSet<>();
                blockList.forEach(block -> {
                    Set<String> exist = redisTemplate.opsForZSet().rangeByScore(cacheKey,block.getNumber(),block.getNumber());
                    if(exist.size()==0){
                        // 在缓存中不存在的才放入缓存
                        tupleSet.add(new DefaultTypedTuple(JSON.toJSONString(block),block.getNumber().doubleValue()));
                    }
                });
                if(tupleSet.size()>0){
                    redisTemplate.opsForZSet().add(cacheKey, tupleSet);
                }
            }

            long size = redisTemplate.opsForZSet().size(cacheKey);
            if(size>500000){
                // 更新后的缓存条目数量大于所规定的数量，则需要删除最旧的 (size-maxItemNum)个
                redisTemplate.opsForZSet().removeRange(cacheKey,0,size-500000);
            }
        });

        chainsConfig.getChainIds().forEach(chainId->{
            String cacheKey = "platon:server:chain_" + chainId + ":transactions";
            TransactionExample condition = new TransactionExample();
            condition.createCriteria().andChainIdEqualTo(chainId);
            condition.setOrderByClause("block_number desc,transaction_index desc");
            for(int i=0;i<500;i++){
                PageHelper.startPage(i+1,1000);
                List<Transaction> transactionList = transactionMapper.selectByExample(condition);
                if(transactionList.size()==0) break;

                Set<ZSetOperations.TypedTuple<String>> tupleSet = new HashSet<>();
                transactionList.forEach(transaction -> {
                    Set<String> exist = redisTemplate.opsForZSet().rangeByScore(cacheKey,transaction.getSequence(),transaction.getSequence());
                    if(exist.size()==0){
                        // 在缓存中不存在的才放入缓存
                        tupleSet.add(new DefaultTypedTuple(JSON.toJSONString(transaction),transaction.getSequence().doubleValue()));
                    }
                });
                if(tupleSet.size()>0){
                    redisTemplate.opsForZSet().add(cacheKey, tupleSet);
                }
            }
            long size = redisTemplate.opsForZSet().size(cacheKey);
            if(size>500000){
                // 更新后的缓存条目数量大于所规定的数量，则需要删除最旧的 (size-maxItemNum)个
                long count = redisTemplate.opsForZSet().removeRange(cacheKey,0,size-500000);
            }
        });
        return "success";
    }

    @Autowired
    private StompCacheInitializer cacheInitializer;
    @GetMapping("/refreshStompCache")
    public String refreshStompCache(){
        cacheInitializer.initCache();
        return "success";
    }
}