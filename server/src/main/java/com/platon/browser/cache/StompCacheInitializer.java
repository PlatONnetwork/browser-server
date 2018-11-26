package com.platon.browser.cache;

import com.alibaba.fastjson.JSON;
import com.maxmind.geoip.Location;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.NodeMapper;
import com.platon.browser.dao.mapper.StatisticMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.dto.IndexInfo;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.StatisticInfo;
import com.platon.browser.dto.StatisticItem;
import com.platon.browser.dto.block.BlockInfo;
import com.platon.browser.dto.block.BlockItem;
import com.platon.browser.dto.cache.BlockInit;
import com.platon.browser.dto.cache.LimitQueue;
import com.platon.browser.dto.cache.TransactionInit;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.transaction.TransactionInfo;
import com.platon.browser.dto.transaction.TransactionItem;
import com.platon.browser.enums.NodeType;
import com.platon.browser.req.block.BlockPageReq;
import com.platon.browser.req.transaction.TransactionPageReq;
import com.platon.browser.service.RedisCacheService;
import com.platon.browser.service.StompCacheService;
import com.platon.browser.util.GeoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 从数据库加载最新数据来初始化缓存
 */
@Component
public class StompCacheInitializer {

    private final Logger logger = LoggerFactory.getLogger(StompCacheInitializer.class);

    @Autowired
    private NodeMapper nodeMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private StompCacheService stompCacheService;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    // 交易TPS统计时间间隔, 单位：分钟
    @Value("${platon.transaction.tps.statistic.interval}")
    private int transactionTpsStatisticInterval;

    @Value("${platon.redis.block.cache.key}")
    private String blockCacheKeyTemplate;
    @Value("${platon.redis.transaction.cache.key}")
    private String transactionCacheKeyTemplate;

    @PostConstruct
    public void initCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            Set<NodeInfo> nodeInfoList = stompCacheService.getNodeInfoSet(chainId);
            if(nodeInfoList.size()==0){
                logger.info("节点缓存为空, 执行初始化...");
                initNodeCache(chainId);
            }
            IndexInfo indexInfo = stompCacheService.getIndexInfo(chainId);
            if(indexInfo.getCurrentHeight()==0){
                logger.info("指标缓存为空, 执行初始化...");
                initIndexCache(chainId);
            }
            StatisticInfo statisticInfo = stompCacheService.getStatisticInfo(chainId);
            if(statisticInfo.getLowestBlockNumber()==0){
                logger.info("统计缓存为空, 执行初始化...");
                initStatisticCache(chainId);
            }
            BlockInit blockInit = stompCacheService.getBlockInit(chainId);
            if(blockInit.getList().size()==0){
                logger.info("区块缓存为空, 执行初始化...");
                initBlockCache(chainId);
            }
            TransactionInit transactionInit = stompCacheService.getTransactionInit(chainId);
            if(transactionInit.getList().size()==0){
                logger.info("交易缓存为空, 执行初始化...");
                initTransactionCache(chainId);
            }
        });
    }

    /**
     * 更新节点信息缓存
     */
    public void initNodeCache(String chainId){
        NodeExample condition = new NodeExample();
        condition.createCriteria().andChainIdEqualTo(chainId);
        List<Node> nodeList = nodeMapper.selectByExample(condition);
        List<NodeInfo> nodeInfoList = new ArrayList<>();
        nodeList.forEach(node -> {
            NodeInfo bean = new NodeInfo();
            BeanUtils.copyProperties(node,bean);
            Location location = GeoUtil.getLocation(node.getIp());
            bean.setLongitude(location.longitude);
            bean.setLatitude(location.latitude);
            nodeInfoList.add(bean);
        });
        stompCacheService.updateNodeCache(nodeInfoList,true,chainId);
    }

    /**
     * 更新指标信息缓存
     */
    public void initIndexCache(String chainId){
        IndexInfo indexInfo = new IndexInfo();

        RespPage<BlockItem> page = redisCacheService.getBlockPage(chainId,1,1);

        if(page.getData().size()==0){
            indexInfo.setNode("");
            indexInfo.setCurrentHeight(0);
        }else{
            BlockItem block = page.getData().get(0);
            indexInfo.setCurrentHeight(block.getHeight());
            indexInfo.setNode(block.getMiner());
        }

        // 取当前交易笔数
        TransactionExample transactionExample = new TransactionExample();
        transactionExample.createCriteria().andChainIdEqualTo(chainId);
        long transactionCount = transactionMapper.countByExample(transactionExample);
        indexInfo.setCurrentTransaction(transactionCount);


        // 取共识节点数
        NodeExample nodeExample = new NodeExample();
        nodeExample.createCriteria().andChainIdEqualTo(chainId)
                .andNodeTypeEqualTo(NodeType.CONSENSUS.code);
        long nodeCount = nodeMapper.countByExample(nodeExample);
        indexInfo.setConsensusNodeAmount(nodeCount);

        // 取地址数
        long addressCount = statisticMapper.countAddress(chainId);
        indexInfo.setAddressAmount(addressCount);

        // 未知如何获取相关数据，暂时设置为0 -- 2018/10/30
        indexInfo.setProportion(0);
        indexInfo.setTicketPrice(0);
        indexInfo.setVoteAmount(0);
        stompCacheService.updateIndexCache(indexInfo,true,chainId);
    }

    /**
     * 更新交易统计信息缓存
     */
    public void initStatisticCache(String chainId){

        String blockCacheKey = blockCacheKeyTemplate.replace("{}",chainId);

        StatisticInfo statisticInfo = new StatisticInfo();


        /*
        RespPage<BlockItem> topPage = redisCacheService.getBlockPage(chainId,1,1);

        List<BlockItem> topList = topPage.getData();
        if(topList.size()==1){
            BlockItem top = topList.get(0);
            statisticInfo.setHighestBlockNumber(top.getHeight());
            statisticInfo.setHighestBlockTimestamp(top.getTimestamp());

            // 先从最高块向前回溯3600个块
            Set<String> bottomBlockSet = redisTemplate.opsForZSet().reverseRange(blockCacheKey,3599,3599);
            if(bottomBlockSet.size()==0){
                // 从后向前累计不足3600个块，则正向取链上第一个块
                bottomBlockSet = redisTemplate.opsForZSet().range(blockCacheKey,0,0);
            }
            if(bottomBlockSet.size()==0){
                statisticInfo.setLowestBlockNumber(top.getHeight());
                statisticInfo.setLowestBlockTimestamp(top.getTimestamp());
                logger.error("找不到初始化统计信息需要的区块信息!!");
            }
            if(bottomBlockSet.size()>0){
                Block bottom = JSON.parseObject(bottomBlockSet.iterator().next(),Block.class);
                statisticInfo.setLowestBlockNumber(bottom.getNumber());
                statisticInfo.setLowestBlockTimestamp(bottom.getTimestamp().getTime());

                long avgTime = (top.getTimestamp()-bottom.getTimestamp().getTime())/top.getHeight();
                statisticInfo.setAvgTime(avgTime);
            }

        }else{
            statisticInfo.setAvgTime(0l);
        }
        */

        // 取当前时间回溯五分钟的交易数统计TPS
        Date endDate = new Date();
        Date startDate = new Date(endDate.getTime()-transactionTpsStatisticInterval*60*1000);
        // 计算TPS时默认使用设置的间隔的秒数作为除数
        long divisor = transactionTpsStatisticInterval*60;
        TransactionExample transactionExample = new TransactionExample();
        transactionExample.createCriteria().andChainIdEqualTo(chainId)
                .andTimestampBetween(startDate,endDate);
        List<Transaction> transactionList = transactionMapper.selectByExample(transactionExample);
        int currentCount = transactionList.size();
        statisticInfo.setTransactionCount(Long.valueOf(currentCount));
        // 当前交易数
        statisticInfo.setCurrent(Long.valueOf(currentCount));
        if(divisor!=0){
            BigDecimal transactionTps = BigDecimal.valueOf(currentCount).divide(BigDecimal.valueOf(divisor),4,BigDecimal.ROUND_DOWN);
            statisticInfo.setMaxTps(transactionTps.longValue());
        }


        // 总交易数
        transactionExample = new TransactionExample();
        transactionExample.createCriteria().andChainIdEqualTo(chainId);
        long currentTransactionCount = transactionMapper.countByExample(transactionExample);
        statisticInfo.setTransactionCount(currentTransactionCount);
        // 有交易的所有区块数
        long blockCount = statisticMapper.countTransactionBlock(chainId);
        statisticInfo.setBlockCount(blockCount);

        // 平均区块交易数=统计最近3600个区块的平均交易数
        // 取最近3600个区块，平均出块时长=(最高块出块时间-最低块出块时间)/实际块数量
        RespPage<BlockItem> blockPage = redisCacheService.getBlockPage(chainId,1,3600);
        int bCount = 0, tCount = 0;
        if(blockPage.getData()==null||blockPage.getData().size()==0){
            // 设置平均区块交易数
            statisticInfo.setAvgTransaction(BigDecimal.ZERO);
            // 设置平均出块时长
            statisticInfo.setAvgTime(BigDecimal.ZERO);
        }else{
            // 设置平均区块交易数
            List<BlockItem> blockItems = blockPage.getData();
            for (BlockItem item : blockItems){
                tCount+=item.getTransaction();
            }
            bCount=blockItems.size();
            if(bCount==0) bCount=1;
            BigDecimal avgTransaction = new BigDecimal(tCount).divide(new BigDecimal(bCount),4, RoundingMode.DOWN);
            statisticInfo.setAvgTransaction(avgTransaction);

            // 设置平均出块时长
            BlockItem top = blockItems.get(0);
            BlockItem bot = blockItems.get(blockItems.size()-1);
            if(top==bot) {
                statisticInfo.setAvgTime(BigDecimal.ZERO);
            }else{
                long diff = top.getTimestamp()-bot.getTimestamp();
                BigDecimal avgTime = new BigDecimal(diff).divide(new BigDecimal(bCount*1000),4,RoundingMode.DOWN);
                statisticInfo.setAvgTime(avgTime);
                statisticInfo.setLowestBlockTimestamp(bot.getTimestamp());
            }
        }


        // 过去24小时交易笔数
        long count = statisticMapper.countTransactionIn24Hours(chainId);
        statisticInfo.setDayTransaction(count);

        // 获取最近100个区块
        RespPage<BlockItem> page = redisCacheService.getBlockPage(chainId,1,100);
        LimitQueue<StatisticItem> limitQueue = new LimitQueue<>(100);
        page.getData().forEach(block->{
            StatisticItem bean = new StatisticItem();
            BeanUtils.copyProperties(block,bean);
            bean.setHeight(block.getHeight());
            bean.setTime(block.getTimestamp());
            limitQueue.offer(bean);
        });
        statisticInfo.setLimitQueue(limitQueue);

        stompCacheService.updateStatisticCache(statisticInfo,true,chainId);

    }

    /**
     * 更新区块列表信息缓存
     */
    public void initBlockCache(String chainId){

        BlockPageReq req = new BlockPageReq();
        req.setCid(chainId);
        req.setPageSize(10);
        RespPage<BlockItem> page = redisCacheService.getBlockPage(req.getCid(),req.getPageNo(),req.getPageSize());

        List<BlockItem> items = page.getData();
        List<BlockInfo> blockInfos = new ArrayList<>();
        long serverTime = System.currentTimeMillis();
        // 由于查数据库的结果是按区块号和交易索引倒排，因此在更新缓存时需要更改为正排
        for(int i=items.size()-1;i>=0;i--) {
            BlockItem block = items.get(i);
            BlockInfo bean = new BlockInfo();
            BeanUtils.copyProperties(block,bean);
            bean.setHeight(block.getHeight());
            bean.setTimestamp(block.getTimestamp());
            bean.setNode(block.getMiner());
            bean.setTransaction(block.getTransaction());
            bean.setServerTime(serverTime);
            blockInfos.add(bean);
        }
        stompCacheService.updateBlockCache(blockInfos,chainId);
    }

    /**
     * 更新交易列表信息缓存
     */
    public void initTransactionCache(String chainId){
        TransactionPageReq req = new TransactionPageReq();
        req.setCid(chainId);
        req.setPageSize(10);
        RespPage<TransactionItem> page = redisCacheService.getTransactionPage(req.getCid(),req.getPageNo(),req.getPageSize());
        List<TransactionInfo> transactionInfos = new LinkedList<>();
        // 由于查数据库的结果是按区块号和交易索引倒排，因此在更新缓存时需要更改为正排
        List<TransactionItem> items = page.getData();
        for (int i=items.size()-1;i>=0;i--){
            TransactionItem transaction = items.get(i);
            TransactionInfo bean = new TransactionInfo();
            BeanUtils.copyProperties(transaction,bean);
            bean.setTxHash(transaction.getTxHash());
            bean.setBlockHeight(transaction.getBlockHeight());
            bean.setTimestamp(transaction.getTimestamp());
            transactionInfos.add(bean);
        }
        stompCacheService.updateTransactionCache(transactionInfos,chainId);
    }
}
