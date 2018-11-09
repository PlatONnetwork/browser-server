package com.platon.browser.service.impl;

import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dto.IndexInfo;
import com.platon.browser.dto.LimitQueue;
import com.platon.browser.dto.StatisticInfo;
import com.platon.browser.dto.StatisticItem;
import com.platon.browser.dto.block.BlockInfo;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.transaction.TransactionInfo;
import com.platon.browser.service.*;
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

/**
 * 缓存服务
 * 提供首页节点信息、指标信息、统计信息、区块信息、交易信息
 */
@Service
public class CacheServiceImpl implements CacheService {

    @Autowired
    private ChainsConfig chainsConfig;

    private final Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

    private Map<String,List<NodeInfo>> nodeInfoMap = new ConcurrentHashMap<>();
    private Map<String,IndexInfo> indexInfoMap = new ConcurrentHashMap<>();
    private Map<String,StatisticInfo> statisticInfoMap = new ConcurrentHashMap<>();
    private Map<String,LimitQueue<BlockInfo>> blockInfoMap = new ConcurrentHashMap<>();
    private Map<String,LimitQueue<TransactionInfo>> transactionInfoMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void init(){
        chainsConfig.getChainIds().forEach(chainId -> {
            nodeInfoMap.put(chainId,new ArrayList<>());
            indexInfoMap.put(chainId,new IndexInfo());
            StatisticInfo statisticInfo = new StatisticInfo();
            statisticInfo.setLimitQueue(new LimitQueue<>(100));
            statisticInfoMap.put(chainId,statisticInfo);
            blockInfoMap.put(chainId,new LimitQueue<>(10));
            transactionInfoMap.put(chainId,new LimitQueue<>(10));
        });
    }

    @Override
    public List<NodeInfo> getNodeInfoList(String chainId) {
        return Collections.unmodifiableList(nodeInfoMap.get(chainId));
    }

    @Override
    public void updateNodeInfoList(List<NodeInfo> nodeInfos,boolean override, String chainId) {
        logger.debug("更新链【ID={}】的节点缓存",chainId);
        List<NodeInfo> cache = nodeInfoMap.get(chainId);
        synchronized (cache){
            if(override){
                cache.clear();
            }
            cache.addAll(nodeInfos);
        }
    }

    @Override
    public IndexInfo getIndexInfo(String chainId) {
        return indexInfoMap.get(chainId);
    }

    @Override
    public void updateIndexInfo(IndexInfo indexInfo, boolean override, String chainId) {
        logger.debug("更新链【ID={}】的指标缓存",chainId);
        IndexInfo cache = indexInfoMap.get(chainId);
        synchronized (cache){
            if(override){
                BeanUtils.copyProperties(indexInfo,cache);
            }else{
                if(StringUtils.isNotBlank(indexInfo.getNode())){
                    cache.setNode(indexInfo.getNode());
                }
                if(indexInfo.getCurrentHeight()!=0){
                    cache.setCurrentHeight(indexInfo.getCurrentHeight());
                }
                if(indexInfo.getConsensusNodeAmount()!=0){
                    cache.setConsensusNodeAmount(cache.getConsensusNodeAmount()+indexInfo.getConsensusNodeAmount());
                }
            }
        }
    }

    @Override
    public StatisticInfo getStatisticInfo(String chainId) {
        StatisticInfo cache = statisticInfoMap.get(chainId);
        StatisticInfo copy = new StatisticInfo();
        BeanUtils.copyProperties(cache,copy);
        return copy;
    }

    @Override
    public void updateStatisticInfo(StatisticInfo statisticInfo, boolean override, String chainId) {
        logger.debug("更新链【ID={}】的统计缓存",chainId);
        StatisticInfo cache = statisticInfoMap.get(chainId);
        synchronized (cache){
            if(override){
                BeanUtils.copyProperties(statisticInfo,cache);
            }else{
                if(statisticInfo.getCurrent()!=null){
                    // 当前交易数
                    cache.setCurrent(statisticInfo.getCurrent());
                }
                if(statisticInfo.getMaxTps()!=null){
                    cache.setMaxTps(statisticInfo.getMaxTps());
                }

                if(statisticInfo.getBlockCount()!=null){
                    cache.setBlockCount(cache.getBlockCount()+statisticInfo.getBlockCount());
                }
                if(statisticInfo.getTransactionCount()!=null){
                    cache.setTransactionCount(cache.getTransactionCount()+statisticInfo.getTransactionCount());
                }
                if(statisticInfo.getBlockCount()!=null&&statisticInfo.getBlockCount()!=0&&statisticInfo.getTransactionCount()!=null){
                    cache.setAvgTransaction(BigDecimal.valueOf(cache.getTransactionCount()/cache.getBlockCount()));
                }
                if(statisticInfo.getHighestBlockNumber()!=null){
                    cache.setHighestBlockNumber(statisticInfo.getHighestBlockNumber());
                    cache.setAvgTime((cache.getHighestBlockNumber()-cache.getLowestBlockNumber())/cache.getHighestBlockNumber());
                }
                if(statisticInfo.getDayTransaction()!=null){
                    cache.setDayTransaction(cache.getDayTransaction()+statisticInfo.getDayTransaction());
                }
                if(statisticInfo.getBlockStatisticList()!=null){
                    Map<Long, StatisticItem> map = new HashMap<>();
                    LimitQueue<StatisticItem> limitQueue = cache.getLimitQueue();
                    limitQueue.elements().forEach(statisticItem -> map.put(statisticItem.getHeight(),statisticItem));
                    statisticInfo.getBlockStatisticList().forEach(statisticItem -> {
                        StatisticItem item = map.get(statisticItem.getHeight());
                        if(item==null){
                            limitQueue.offer(statisticItem);
                        }else{
                            item.setTransaction(item.getTransaction()+statisticItem.getTransaction());
                        }
                    });
                }
            }
        }
    }

    @Override
    public List<BlockInfo> getBlockInfoList(String chainId) {
        LimitQueue<BlockInfo> cache = blockInfoMap.get(chainId);
        return Collections.unmodifiableList(cache.elements());
    }

    @Override
    public void updateBlockInfoList(List<BlockInfo> blockInfos, String chainId) {
        logger.debug("更新链【ID={}】的块列表缓存",chainId);
        LimitQueue<BlockInfo> cache = blockInfoMap.get(chainId);
        synchronized (cache){
            blockInfos.forEach(e->cache.offer(e));
        }
    }

    @Override
    public List<TransactionInfo> getTransactionInfoList(String chainId) {
        LimitQueue<TransactionInfo> cache = transactionInfoMap.get(chainId);
        return Collections.unmodifiableList(cache.elements());
    }

    @Override
    public void updateTransactionInfoList(List<TransactionInfo> transactionInfos, String chainId) {
        logger.debug("更新链【ID={}】的交易列表缓存",chainId);
        LimitQueue<TransactionInfo> cache = transactionInfoMap.get(chainId);
        synchronized (cache){
            transactionInfos.forEach(e->cache.offer(e));
        }
    }
}
