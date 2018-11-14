package com.platon.browser.service.impl;

import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dto.*;
import com.platon.browser.dto.block.BlockInfo;
import com.platon.browser.dto.cache.*;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.transaction.TransactionInfo;
import com.platon.browser.service.CacheService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CacheServiceImpl implements CacheService {

    @Autowired
    private ChainsConfig chainsConfig;

    private final Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

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
                    index.setNode(indexInfo.getNode());
                    changed=true;
                }
                if(indexInfo.getCurrentHeight()!=0){
                    index.setCurrentHeight(indexInfo.getCurrentHeight());
                    changed=true;
                }
                if(indexInfo.getConsensusNodeAmount()!=0){
                    index.setConsensusNodeAmount(index.getConsensusNodeAmount()+indexInfo.getConsensusNodeAmount());
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
                if(statisticInfo.getCurrent()!=null){
                    // 当前交易数
                    cache.setCurrent(statisticInfo.getCurrent());
                    changed = true;
                }
                if(statisticInfo.getMaxTps()!=null){
                    cache.setMaxTps(statisticInfo.getMaxTps());
                    changed = true;
                }

                if(statisticInfo.getBlockCount()!=null){
                    cache.setBlockCount(cache.getBlockCount()+statisticInfo.getBlockCount());
                    changed = true;
                }
                if(statisticInfo.getTransactionCount()!=null){
                    cache.setTransactionCount(cache.getTransactionCount()+statisticInfo.getTransactionCount());
                    changed = true;
                }
                if(statisticInfo.getBlockCount()!=null&&statisticInfo.getBlockCount()!=0&&statisticInfo.getTransactionCount()!=null){
                    cache.setAvgTransaction(BigDecimal.valueOf(cache.getTransactionCount()/cache.getBlockCount()));
                    changed = true;
                }
                if(statisticInfo.getHighestBlockNumber()!=null){
                    cache.setHighestBlockNumber(statisticInfo.getHighestBlockNumber());
                    cache.setHighestBlockTimestamp(statisticInfo.getHighestBlockTimestamp());
                    cache.setAvgTime((cache.getHighestBlockTimestamp()-cache.getLowestBlockTimestamp())/cache.getHighestBlockNumber());
                    changed = true;
                }
                if(statisticInfo.getDayTransaction()!=null){
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
        LimitQueue<BlockInfo> cache = blockInitMap.get(chainId);
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
        }
    }

    @Override
    public void updateBlockCache(List<BlockInfo> blockInfos, String chainId) {
        logger.debug("更新链【ID={}】的块列表缓存",chainId);
        LimitQueue<BlockInfo> init = blockInitMap.get(chainId);
        ReentrantReadWriteLock lock = init.getLock();
        lock.writeLock().lock();
        try{
            blockInfos.forEach(e->init.offer(e));
            init.setChanged(true);
        }finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public TransactionInit getTransactionInit(String chainId) {
        LimitQueue<TransactionInfo> cache = transactionInitMap.get(chainId);
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
        }
    }

    @Override
    public void updateTransactionCache(List<TransactionInfo> transactionInfos, String chainId) {
        logger.debug("更新链【ID={}】的交易列表缓存",chainId);
        LimitQueue<TransactionInfo> init = transactionInitMap.get(chainId);
        ReentrantReadWriteLock lock = init.getLock();
        lock.writeLock().lock();
        try{
            transactionInfos.forEach(e->init.offer(e));
            init.setChanged(true);
        }finally {
            lock.writeLock().unlock();
        }
    }
}
