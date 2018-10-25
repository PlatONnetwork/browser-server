package com.platon.browser.service;

import com.platon.browser.common.dto.*;

import java.util.List;

/**
 * 进程缓存服务
 */
public interface CacheService {
    /**
     * 节点信息
     */
    List<NodeInfo> getNodeInfoList();
    void updateNodeInfoList(NodeInfo... nodeInfos);

    /**
     * 指标信息
     */
    IndexInfo getIndexInfo();
    void updateIndexInfo(IndexInfo indexInfo);

    /**
     * 交易统计信息
     */
    StatisticInfo getStatisticInfo();
    void updateStatisticInfo(StatisticInfo statisticInfo);

    /**
     * 区块列表信息
     */
    List<BlockInfo> getBlockInfoList();
    void updateBlockInfoList(BlockInfo... blockInfos);

    /**
     * 交易列表信息
     */
    List<TransactionInfo> getTransactionInfoList();
    void updateTransactionInfoList(TransactionInfo... transactionInfos);
}
