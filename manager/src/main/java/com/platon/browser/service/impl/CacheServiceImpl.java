package com.platon.browser.service.impl;

import com.platon.browser.dto.IndexInfo;
import com.platon.browser.dto.StatisticInfo;
import com.platon.browser.dto.block.BlockInfo;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.transaction.TransactionInfo;
import com.platon.browser.enums.ChainEnum;
import com.platon.browser.service.CacheService;
import com.platon.browser.util.LimitQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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
        logger.debug("更新链【{}-{}】的节点缓存",chainId.desc,chainId.code);
        synchronized (chainId){
            List<NodeInfo> nodeInfoList = nodeInfoMap.get(chainId);
            if(override){
                nodeInfoMap.put(chainId,nodeInfos);
            }
            nodeInfoList.addAll(nodeInfos);
        }
    }

    @Override
    public IndexInfo getIndexInfo(ChainEnum chainId) {
        return indexInfoMap.get(chainId);
    }

    @Override
    public void updateIndexInfo(IndexInfo indexInfo, ChainEnum chainId) {
        logger.debug("更新链【{}-{}】的指标缓存",chainId.desc,chainId.code);
        synchronized (chainId){
            IndexInfo cache = indexInfoMap.get(chainId);
            BeanUtils.copyProperties(indexInfo,cache);
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
    public void updateStatisticInfo(StatisticInfo statisticInfo, ChainEnum chainId) {
        logger.debug("更新链【{}-{}】的统计缓存",chainId.desc,chainId.code);
        synchronized (chainId){
            StatisticInfo cache = statisticInfoMap.get(chainId);
            BeanUtils.copyProperties(statisticInfo,cache);
        }
    }

    @Override
    public List<BlockInfo> getBlockInfoList(ChainEnum chainId) {
        LimitQueue<BlockInfo> limitQueue = blockInfoMap.get(chainId);
        return Collections.unmodifiableList(limitQueue.elements());
    }

    @Override
    public void updateBlockInfoList(List<BlockInfo> blockInfos, ChainEnum chainId) {
        logger.debug("更新链【{}-{}】的块列表缓存",chainId.desc,chainId.code);
        synchronized (chainId){
            LimitQueue<BlockInfo> limitQueue = blockInfoMap.get(chainId);
            blockInfos.forEach(e->limitQueue.offer(e));
        }
    }

    @Override
    public List<TransactionInfo> getTransactionInfoList(ChainEnum chainId) {
        LimitQueue<TransactionInfo> limitQueue = transactionInfoMap.get(chainId);
        return Collections.unmodifiableList(limitQueue.elements());
    }

    @Override
    public void updateTransactionInfoList(List<TransactionInfo> transactionInfos, ChainEnum chainId) {
        logger.debug("更新链【{}-{}】的交易列表缓存",chainId.desc,chainId.code);
        synchronized (chainId){
            LimitQueue<TransactionInfo> limitQueue = transactionInfoMap.get(chainId);
            transactionInfos.forEach(e->limitQueue.offer(e));
        }
    }
}
