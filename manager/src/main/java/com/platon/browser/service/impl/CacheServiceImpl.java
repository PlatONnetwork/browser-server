package com.platon.browser.service.impl;

import com.platon.browser.dto.IndexInfo;
import com.platon.browser.dto.StatisticInfo;
import com.platon.browser.dto.StatisticItem;
import com.platon.browser.dto.block.BlockInfo;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.transaction.TransactionInfo;
import com.platon.browser.enums.ChainEnum;
import com.platon.browser.service.CacheService;
import com.platon.browser.util.LimitQueue;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 缓存服务
 * 提供首页节点信息、统计信息、区块信息、交易信息
 */
@Service
public class CacheServiceImpl implements CacheService {

    private final Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

    private Map<ChainEnum,List<NodeInfo>> nodeInfoMap = new ConcurrentHashMap<>();
    private Map<ChainEnum,IndexInfo> indexInfoMap = new ConcurrentHashMap<>();
    private Map<ChainEnum,StatisticInfo> statisticInfoMap = new ConcurrentHashMap<>();
    private Map<ChainEnum,LimitQueue<BlockInfo>> blockInfoMap = new ConcurrentHashMap<>();
    private Map<ChainEnum,LimitQueue<TransactionInfo>> transactionInfoMap = new ConcurrentHashMap<>();

    public CacheServiceImpl(){
        Arrays.asList(ChainEnum.values()).forEach(chainId -> {
            nodeInfoMap.put(chainId,new ArrayList<>());
            indexInfoMap.put(chainId,new IndexInfo());
            statisticInfoMap.put(chainId,new StatisticInfo());
            blockInfoMap.put(chainId,new LimitQueue<>(10));
            transactionInfoMap.put(chainId,new LimitQueue<>(10));
        });
    }

    @Override
    public List<NodeInfo> getNodeInfoList(ChainEnum chainId) {
        return Collections.unmodifiableList(nodeInfoMap.get(chainId));
    }

    @Override
    public void updateNodeInfoList(List<NodeInfo> nodeInfos,boolean override, ChainEnum chainId) {
        logger.info("更新链【{}-{}】的节点缓存",chainId.desc,chainId.code);
        List<NodeInfo> cache = nodeInfoMap.get(chainId);
        synchronized (cache){
            if(override){
                cache.clear();
            }
            cache.addAll(nodeInfos);
        }
    }

    @Override
    public IndexInfo getIndexInfo(ChainEnum chainId) {
        return indexInfoMap.get(chainId);
    }

    @Override
    public void updateIndexInfo(IndexInfo indexInfo, boolean override, ChainEnum chainId) {
        logger.info("更新链【{}-{}】的指标缓存",chainId.desc,chainId.code);
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
    public StatisticInfo getStatisticInfo(ChainEnum chainId) {
        StatisticInfo cache = statisticInfoMap.get(chainId);
        StatisticInfo copy = new StatisticInfo();
        BeanUtils.copyProperties(cache,copy);
        return copy;
    }

    @Override
    public void updateStatisticInfo(StatisticInfo statisticInfo, boolean override, ChainEnum chainId) {
        logger.info("更新链【{}-{}】的统计缓存",chainId.desc,chainId.code);
        StatisticInfo cache = statisticInfoMap.get(chainId);
        synchronized (cache){
            if(override){
                BeanUtils.copyProperties(statisticInfo,cache);
            }else{
                if(statisticInfo.getBlockCount()!=0){
                    cache.setBlockCount(cache.getBlockCount()+statisticInfo.getBlockCount());
                }
                if(statisticInfo.getTransactionCount()!=0){
                    cache.setCurrent(cache.getCurrent()+statisticInfo.getCurrent());
                    cache.setTransactionCount(cache.getTransactionCount()+statisticInfo.getTransactionCount());
                }
                if(statisticInfo.getBlockCount()!=0||statisticInfo.getTransactionCount()!=0){
                    cache.setAvgTransaction(BigDecimal.valueOf(cache.getTransactionCount()/cache.getBlockCount()));
                }
                if(statisticInfo.getHighestBlockNumber()!=0){
                    cache.setHighestBlockNumber(statisticInfo.getHighestBlockNumber());
                    cache.setAvgTime((cache.getHighestBlockNumber()-cache.getLowestBlockNumber())/cache.getHighestBlockNumber());
                }
                if(statisticInfo.getDayTransaction()!=0){
                    cache.setDayTransaction(cache.getDayTransaction()+statisticInfo.getDayTransaction());
                }
                if(statisticInfo.getBlockStatisticList()!=null){
                    Map<Long, StatisticItem> map = new HashMap<>();
                    List<StatisticItem> cacheStatisticItemList = cache.getBlockStatisticList();
                    cacheStatisticItemList.forEach(statisticItem -> map.put(statisticItem.getHeight(),statisticItem));
                    statisticInfo.getBlockStatisticList().forEach(statisticItem -> {
                        StatisticItem item = map.get(statisticItem.getHeight());
                        if(item==null){
                            cacheStatisticItemList.add(statisticItem);
                        }else{
                            item.setTransaction(item.getTransaction()+statisticItem.getTransaction());
                        }
                    });
                }
            }
        }
    }

    @Override
    public List<BlockInfo> getBlockInfoList(ChainEnum chainId) {
        LimitQueue<BlockInfo> cache = blockInfoMap.get(chainId);
        return Collections.unmodifiableList(cache.elements());
    }

    @Override
    public void updateBlockInfoList(List<BlockInfo> blockInfos, ChainEnum chainId) {
        logger.info("更新链【{}-{}】的块列表缓存",chainId.desc,chainId.code);
        LimitQueue<BlockInfo> cache = blockInfoMap.get(chainId);
        synchronized (cache){
            blockInfos.forEach(e->cache.offer(e));
        }
    }

    @Override
    public List<TransactionInfo> getTransactionInfoList(ChainEnum chainId) {
        LimitQueue<TransactionInfo> cache = transactionInfoMap.get(chainId);
        return Collections.unmodifiableList(cache.elements());
    }

    @Override
    public void updateTransactionInfoList(List<TransactionInfo> transactionInfos, ChainEnum chainId) {
        logger.info("更新链【{}-{}】的交易列表缓存",chainId.desc,chainId.code);
        LimitQueue<TransactionInfo> cache = transactionInfoMap.get(chainId);
        synchronized (cache){
            transactionInfos.forEach(e->cache.offer(e));
        }
    }
}
