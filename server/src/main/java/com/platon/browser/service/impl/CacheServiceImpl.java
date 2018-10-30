package com.platon.browser.service.impl;

import com.platon.browser.dto.IndexInfo;
import com.platon.browser.dto.StatisticInfo;
import com.platon.browser.dto.block.BlockInfo;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.transaction.TransactionInfo;
import com.platon.browser.service.CacheService;
import com.platon.browser.util.LimitQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 缓存服务
 * 提供首页节点信息、统计信息、区块信息、交易信息
 */
@Service
public class CacheServiceImpl implements CacheService {

    private final Logger logger = LoggerFactory.getLogger(CacheServiceImpl.class);

    private ReentrantReadWriteLock nodeInfoListLock = new ReentrantReadWriteLock();
    private List<NodeInfo> nodeInfoList = new ArrayList<>();

    private ReentrantReadWriteLock indexInfoLock = new ReentrantReadWriteLock();
    private IndexInfo indexInfo = new IndexInfo();

    private ReentrantReadWriteLock statisticInfoLock = new ReentrantReadWriteLock();
    private StatisticInfo statisticInfo = new StatisticInfo();

    private ReentrantReadWriteLock blockInfoListLock = new ReentrantReadWriteLock();
    private LimitQueue<BlockInfo> blockInfoList = new LimitQueue<>(10);

    private ReentrantReadWriteLock transactionInfoListLock = new ReentrantReadWriteLock();
    private LimitQueue<TransactionInfo> transactionInfoList = new LimitQueue<>(10);

    @Override
    public List<NodeInfo> getNodeInfoList() {
        nodeInfoListLock.readLock().lock();
        try{
            return Collections.unmodifiableList(nodeInfoList);
        }finally {
            nodeInfoListLock.readLock().unlock();
        }
    }

    @Override
    public void updateNodeInfoList(boolean override, List<NodeInfo> nodeInfos) {
        nodeInfoListLock.writeLock().lock();
        try{
            if(override){
                nodeInfoList.clear();
            }
            nodeInfoList.addAll(nodeInfos);
        }finally {
            nodeInfoListLock.writeLock().unlock();
        }

    }

    @Override
    public IndexInfo getIndexInfo() {
        indexInfoLock.readLock().lock();
        try{
            IndexInfo copy = new IndexInfo();
            BeanUtils.copyProperties(indexInfo,copy);
            return copy;
        }finally {
            indexInfoLock.readLock().unlock();
        }
    }

    @Override
    public void updateIndexInfo(IndexInfo indexInfo) {
        indexInfoLock.writeLock().lock();
        try{
            BeanUtils.copyProperties(indexInfo,this.indexInfo);
        }finally {
            indexInfoLock.writeLock().unlock();
        }
    }

    @Override
    public StatisticInfo getStatisticInfo() {
        statisticInfoLock.readLock().lock();
        try{
            StatisticInfo copy = new StatisticInfo();
            BeanUtils.copyProperties(statisticInfo,copy);
            return copy;
        }finally {
            statisticInfoLock.readLock().unlock();
        }
    }

    @Override
    public void updateStatisticInfo(StatisticInfo statisticInfo) {
        statisticInfoLock.writeLock().lock();
        try{
            BeanUtils.copyProperties(statisticInfo,this.statisticInfo);
        }finally {
            statisticInfoLock.writeLock().unlock();
        }
    }

    @Override
    public List<BlockInfo> getBlockInfoList() {
        blockInfoListLock.readLock().lock();
        try{
            return blockInfoList.elements();
        }finally {
            blockInfoListLock.readLock().unlock();
        }
    }

    @Override
    public void updateBlockInfoList(List<BlockInfo> blockInfos) {
        blockInfoListLock.writeLock().lock();
        try{
            blockInfos.forEach(e->blockInfoList.offer(e));
        }finally {
            blockInfoListLock.writeLock().unlock();
        }
    }

    @Override
    public List<TransactionInfo> getTransactionInfoList() {
        transactionInfoListLock.readLock().lock();
        try{
            return transactionInfoList.elements();
        }finally {
            transactionInfoListLock.readLock().unlock();
        }
    }

    @Override
    public void updateTransactionInfoList(List<TransactionInfo> transactionInfos) {
        transactionInfoListLock.writeLock().lock();
        try{
            transactionInfos.forEach(e->transactionInfoList.offer(e));
        }finally {
            transactionInfoListLock.writeLock().unlock();
        }
    }
}
