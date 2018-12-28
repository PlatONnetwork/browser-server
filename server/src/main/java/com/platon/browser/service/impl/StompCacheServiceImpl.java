package com.platon.browser.service.impl;

import com.alibaba.fastjson.JSON;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.mapper.StatisticMapper;
import com.platon.browser.dto.IndexInfo;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.StatisticInfo;
import com.platon.browser.dto.StatisticItem;
import com.platon.browser.dto.block.BlockInfo;
import com.platon.browser.dto.block.BlockItem;
import com.platon.browser.dto.cache.BlockInit;
import com.platon.browser.dto.cache.LimitQueue;
import com.platon.browser.dto.cache.NodeIncrement;
import com.platon.browser.dto.cache.TransactionInit;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.transaction.TransactionInfo;
import com.platon.browser.dto.transaction.TransactionItem;
import com.platon.browser.service.RedisCacheService;
import com.platon.browser.service.StompCacheService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 缓存服务
 * 提供首页节点信息、指标信息、统计信息、区块信息、交易信息
 */
@Service
public class StompCacheServiceImpl implements StompCacheService {

    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Value("${platon.redis.block.cache.key}")
    private String blockCacheKeyTemplate;

    private final Logger logger = LoggerFactory.getLogger(StompCacheServiceImpl.class);

    // 初始数据Map
    private Map<String,Set<NodeInfo>> nodeInitMap = new ConcurrentHashMap<>();
    private Map<String,IndexInfo> indexInitMap = new ConcurrentHashMap<>();
    private Map<String,StatisticInfo> statisticInitMap = new ConcurrentHashMap<>();
    private Map<String, LimitQueue<BlockInfo>> blockInitMap = new ConcurrentHashMap<>();
    private Map<String,LimitQueue<TransactionInfo>> transactionInitMap = new ConcurrentHashMap<>();

    // 增量Map
    private Map<String, NodeIncrement> nodeIncrementMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void init(){
        chainsConfig.getChainIds().forEach(chainId -> {
            nodeInitMap.put(chainId,new HashSet<>());
            indexInitMap.put(chainId,new IndexInfo());
            StatisticInfo statisticInfo = new StatisticInfo();
            statisticInfo.setLimitQueue(new LimitQueue<>(100));
            statisticInitMap.put(chainId,statisticInfo);
            blockInitMap.put(chainId,new LimitQueue<>(10));
            transactionInitMap.put(chainId,new LimitQueue<>(10));

            // 初始化增量Map
            nodeIncrementMap.put(chainId,new NodeIncrement());
        });
    }

    // 取增量数据
    @Override
    public NodeIncrement getNodeIncrement(String chainId){
        NodeIncrement increment = nodeIncrementMap.get(chainId);
        ReentrantReadWriteLock lock = increment.getLock();
        lock.readLock().lock();
        try{
            NodeIncrement copy = new NodeIncrement();
            BeanUtils.copyProperties(increment,copy);
            // 取完此增量节点信息推送后，设置为未更改状态
            increment.setChanged(false);
            // 返回副本
            return copy;
        }finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Set<NodeInfo> getNodeInfoSet(String chainId) {
        Set<NodeInfo> nodeInfoSet = nodeInitMap.get(chainId);
        return Collections.unmodifiableSet(nodeInfoSet);
    }

    @Override
    public void updateNodeCache(List<NodeInfo> nodeInfos,boolean override, String chainId) {
        logger.debug("更新链【ID={}】的节点缓存",chainId);
        Set<NodeInfo> init = nodeInitMap.get(chainId);
        synchronized (init){
            if(override){
                init.clear();
            }
            init.addAll(nodeInfos);
        }

        logger.debug("更新链【ID={}】的节点增量缓存",chainId);
        NodeIncrement increment = nodeIncrementMap.get(chainId);
        ReentrantReadWriteLock lock = increment.getLock();
        lock.writeLock().lock();
        try{
            increment.getIncrement().clear();
            increment.getIncrement().addAll(nodeInfos);
            increment.setChanged(true);
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public IndexInfo getIndexInfo(String chainId) {
        IndexInfo cache = indexInitMap.get(chainId);
        ReentrantReadWriteLock lock = cache.getLock();
        lock.readLock().lock();
        try{
            IndexInfo copy = new IndexInfo();
            BeanUtils.copyProperties(cache,copy);
            cache.setChanged(false);
            return copy;
        }finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void updateIndexCache(IndexInfo indexInfo, boolean override, String chainId) {
        logger.debug("更新链【ID={}】的指标缓存",chainId);
        IndexInfo index = indexInitMap.get(chainId);
        ReentrantReadWriteLock lock = index.getLock();
        lock.writeLock().lock();
        try{
            boolean changed = false;
            if(override){
                BeanUtils.copyProperties(indexInfo,index);
                changed=true;
            }else{
                if(StringUtils.isNotBlank(indexInfo.getNode())){
                    // 更新当前出块节点
                    index.setNode(indexInfo.getNode());
                    changed=true;
                }
                if(indexInfo.getCurrentHeight()>0){
                    // 更新当前区块高度
                    index.setCurrentHeight(indexInfo.getCurrentHeight());
                    changed=true;
                }
                if(indexInfo.getConsensusNodeAmount()>0){
                    // 更新共识节点数
                    index.setConsensusNodeAmount(index.getConsensusNodeAmount()+indexInfo.getConsensusNodeAmount());
                    changed=true;
                }
                if(indexInfo.getCurrentTransaction()>0){
                    // 更新当前交易数
                    index.setCurrentTransaction(index.getCurrentTransaction()+indexInfo.getCurrentTransaction());
                    changed=true;
                }
            }
            index.setChanged(changed);
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public StatisticInfo getStatisticInfo(String chainId) {

        StatisticInfo cache = statisticInitMap.get(chainId);
        ReentrantReadWriteLock lock = cache.getLock();
        lock.readLock().lock();
        try{
            StatisticInfo copy = new StatisticInfo();
            BeanUtils.copyProperties(cache,copy);
            long count = statisticMapper.countTransactionIn24Hours(chainId);
            copy.setDayTransaction(count);

            List<StatisticItem> itemList = cache.getLimitQueue().list();
            Collections.sort(itemList,(c1,c2)->{
                // 根据区块高度正排
                if(c1.getHeight()>c2.getHeight()) return 1;
                if(c1.getHeight()<c2.getHeight()) return -1;
                return 0;
            });
            copy.setBlockStatisticList(itemList);
            cache.setChanged(false);
            return copy;
        }finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void updateStatisticCache(StatisticInfo statisticInfo, boolean override, String chainId) {
        logger.debug("更新链【ID={}】的统计缓存",chainId);
        StatisticInfo cache = statisticInitMap.get(chainId);
        ReentrantReadWriteLock lock = cache.getLock();
        lock.writeLock().lock();
        try{
            boolean changed = false;
            if(override){
                BeanUtils.copyProperties(statisticInfo,cache);
                changed = true;
            }else{
                if(statisticInfo.getCurrent()>0){
                    // 当前TPS
                    cache.setCurrent(statisticInfo.getCurrent());
                    changed = true;
                }
                if(statisticInfo.getCurrent()==-1){
                    cache.setCurrent(0);
                    changed = true;
                }
                if(statisticInfo.getMaxTps()>0){
                    cache.setMaxTps(statisticInfo.getMaxTps());
                    changed = true;
                }

                if(statisticInfo.getBlockCount()>0){
                    cache.setBlockCount(cache.getBlockCount()+statisticInfo.getBlockCount());
                    changed = true;
                }
                if(statisticInfo.getTransactionCount()>0){
                    cache.setTransactionCount(cache.getTransactionCount()+statisticInfo.getTransactionCount());
                    changed = true;
                }
                if(statisticInfo.getBlockCount()>0&&statisticInfo.getBlockCount()>0&&statisticInfo.getTransactionCount()>0){
                    cache.setAvgTransaction(BigDecimal.valueOf(cache.getTransactionCount()/cache.getBlockCount()));
                    changed = true;
                }
                if(statisticInfo.getHighestBlockNumber()>0){
                    cache.setHighestBlockNumber(statisticInfo.getHighestBlockNumber());
                    cache.setHighestBlockTimestamp(statisticInfo.getHighestBlockTimestamp());

                    String blockCacheKey = blockCacheKeyTemplate.replace("{}",chainId);
                    // 先从最高块向前回溯3600个块
                    Set<String> bottomBlockSet = redisTemplate.opsForZSet().reverseRange(blockCacheKey,3599,3599);
                    if(bottomBlockSet.size()==0){
                        // 从后向前累计不足3600个块，则正向取链上第一个块
                        bottomBlockSet = redisTemplate.opsForZSet().range(blockCacheKey,0,0);
                    }
                    if(bottomBlockSet.size()==0){
                        cache.setLowestBlockNumber(1);
                        cache.setLowestBlockTimestamp(System.currentTimeMillis());
                    }else {
                        Block bottom = JSON.parseObject(bottomBlockSet.iterator().next(),Block.class);
                        cache.setLowestBlockNumber(bottom.getNumber());
                        cache.setLowestBlockTimestamp(bottom.getTimestamp().getTime());
                    }
                    long divider = cache.getHighestBlockNumber()-cache.getLowestBlockNumber();
                    if(divider==0){
                        divider=1;
                    }
                    cache.setAvgTime((cache.getHighestBlockTimestamp()-cache.getLowestBlockTimestamp())/divider/1000);
                    changed = true;
                }
                if(statisticInfo.getDayTransaction()>0){
                    cache.setDayTransaction(cache.getDayTransaction()+statisticInfo.getDayTransaction());
                    changed = true;
                }
                if(statisticInfo.getBlockStatisticList()!=null){
                    Map<Long, StatisticItem> map = new HashMap<>();
                    LimitQueue<StatisticItem> limitQueue = cache.getLimitQueue();
                    List<StatisticItem> itemList = limitQueue.list();
                    Collections.sort(itemList,(c1,c2)->{
                        // 根据区块高度正排
                        if(c1.getHeight()>c2.getHeight()) return 1;
                        if(c1.getHeight()<c2.getHeight()) return -1;
                        return 0;
                    });
                    itemList.forEach(statisticItem -> map.put(statisticItem.getHeight(),statisticItem));
                    statisticInfo.getBlockStatisticList().forEach(statisticItem -> {
                        StatisticItem item = map.get(statisticItem.getHeight());
                        if(item==null){
                            limitQueue.offer(statisticItem);
                        }else{
                            item.setTransaction(item.getTransaction()+statisticItem.getTransaction());
                        }
                    });
                    changed = true;
                }
            }
            cache.setChanged(changed);
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public BlockInit getBlockInit(String chainId) {
        /*LimitQueue<BlockInfo> cache = blockInitMap.get(chainId);
        ReentrantReadWriteLock lock = cache.getLock();
        lock.readLock().lock();
        try{
            BlockInit blockInit = new BlockInit();
            blockInit.setChanged(cache.isChanged());

            List<BlockInfo> blockInfoList = cache.list();
            Collections.sort(blockInfoList,(c1,c2)->{
                // 根据区块高度倒排
                if(c1.getHeight()>c2.getHeight()) return -1;
                if(c1.getHeight()<c2.getHeight()) return 1;
                return 0;
            });
            blockInit.setList(blockInfoList);
            cache.setChanged(false);
            return blockInit;
        }finally {
            lock.readLock().unlock();
        }*/

        // 直接从缓存取最新的10条数据
        RespPage<BlockItem> page = redisCacheService.getBlockPage(chainId,1,10);
        BlockInit blockInit = new BlockInit();
        List<BlockInfo> blockInfoList = new LinkedList<>();
        page.getData().forEach(blockItem -> {
            BlockInfo bean = new BlockInfo();
            blockInfoList.add(bean);
            BeanUtils.copyProperties(blockItem,bean);
            bean.setNode(blockItem.getMiner());
        });
        blockInit.setList(blockInfoList);
        return blockInit;
    }

    @Override
    public void updateBlockCache(List<BlockInfo> blockInfos, String chainId) {
        /*logger.debug("更新链【ID={}】的块列表缓存",chainId);
        LimitQueue<BlockInfo> init = blockInitMap.get(chainId);
        ReentrantReadWriteLock lock = init.getLock();
        lock.writeLock().lock();
        try{
            blockInfos.forEach(e->init.offer(e));
            init.setChanged(true);
        }finally {
            lock.writeLock().unlock();
        }*/
    }

    @Override
    public TransactionInit getTransactionInit(String chainId) {
        /*LimitQueue<TransactionInfo> cache = transactionInitMap.get(chainId);
        ReentrantReadWriteLock lock = cache.getLock();
        lock.readLock().lock();
        try{
            TransactionInit transactionInit = new TransactionInit();
            transactionInit.setChanged(cache.isChanged());
            List<TransactionInfo> transactionInfoList = cache.list();
            Collections.sort(transactionInfoList,(c1,c2)->{
                // 根据区块高度倒排
                if(c1.getBlockHeight()>c2.getBlockHeight()) return -1;
                if(c1.getBlockHeight()<c2.getBlockHeight()) return 1;
                return 0;
            });
            transactionInit.setList(transactionInfoList);
            cache.setChanged(false);
            return transactionInit;
        }finally {
            lock.readLock().unlock();
        }*/

        // 直接从缓存取最新的10条数据
        RespPage<TransactionItem> page = redisCacheService.getTransactionPage(chainId,1,10);
        TransactionInit transactionInit = new TransactionInit();
        List<TransactionInfo> transactionInfoList = new LinkedList<>();
        page.getData().forEach(transactionItem -> {
            TransactionInfo bean = new TransactionInfo();
            transactionInfoList.add(bean);
            BeanUtils.copyProperties(transactionItem,bean);
        });
        transactionInit.setList(transactionInfoList);
        return transactionInit;
    }

    @Override
    public void updateTransactionCache(List<TransactionInfo> transactionInfos, String chainId) {
       /* logger.debug("更新链【ID={}】的交易列表缓存",chainId);
        LimitQueue<TransactionInfo> init = transactionInitMap.get(chainId);
        ReentrantReadWriteLock lock = init.getLock();
        lock.writeLock().lock();
        try{
            transactionInfos.forEach(e->init.offer(e));
            init.setChanged(true);
        }finally {
            lock.writeLock().unlock();
        }*/
    }
}
