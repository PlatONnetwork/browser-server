package com.platon.browser.service.impl;

import com.platon.browser.common.dto.*;
import com.platon.browser.service.CacheService;
import com.platon.browser.util.LimitQueue;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 缓存服务
 * 提供首页节点信息、统计信息、区块信息、交易信息
 */
@Service
public class CacheServiceImpl implements CacheService {

    private List<NodeInfo> nodeInfoList = new ArrayList<>();

    private IndexInfo indexInfo = new IndexInfo();

    private StatisticInfo statisticInfo = new StatisticInfo();

    private LimitQueue<BlockInfo> blockInfoList = new LimitQueue<>(10);

    private LimitQueue<TransactionInfo> transactionInfoList = new LimitQueue<>(10);

    @Override
    public List<NodeInfo> getNodeInfoList() {
        return Collections.unmodifiableList(nodeInfoList);
    }

    @Override
    public void updateNodeInfoList(NodeInfo... nodeInfos) {
        nodeInfoList.addAll(Arrays.asList(nodeInfos));
    }

    @Override
    public IndexInfo getIndexInfo() {
        IndexInfo copy = new IndexInfo();
        BeanUtils.copyProperties(indexInfo,copy);
        return copy;
    }

    @Override
    public void updateIndexInfo(IndexInfo indexInfo) {
        BeanUtils.copyProperties(indexInfo,this.indexInfo);
    }

    @Override
    public StatisticInfo getStatisticInfo() {
        StatisticInfo copy = new StatisticInfo();
        BeanUtils.copyProperties(statisticInfo,copy);
        return copy;
    }

    @Override
    public void updateStatisticInfo(StatisticInfo statisticInfo) {
        BeanUtils.copyProperties(statisticInfo,this.statisticInfo);
    }

    @Override
    public List<BlockInfo> getBlockInfoList() {
        return blockInfoList.elements();
    }

    @Override
    public void updateBlockInfoList(BlockInfo... blockInfos) {
        Arrays.asList(blockInfos).forEach(e->blockInfoList.offer(e));
    }

    @Override
    public List<TransactionInfo> getTransactionInfoList() {
        return transactionInfoList.elements();
    }

    @Override
    public void updateTransactionInfoList(TransactionInfo... transactionInfos) {
        Arrays.asList(transactionInfos).forEach(e->transactionInfoList.offer(e));
    }
}
