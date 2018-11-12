package com.platon.browser.service;

import com.platon.browser.dto.*;
import com.platon.browser.dto.block.BlockInfo;
import com.platon.browser.dto.node.NodeInfo;
import com.platon.browser.dto.transaction.TransactionInfo;

import java.util.List;

/**
 * 进程缓存服务
 */
public interface CacheService {

    // 取增量数据
    NodeIncrement getNodeIncrement(String chainId);
    BlockIncrement getBlockIncrement(String chainId);
    TransactionIncrement getTransactionIncrement(String chainId);

    /**
     * 节点信息
     */
    List<NodeInfo> getNodeInfoList(String chainId);

    /**
     * 更新节点信息列表
     * @param nodeInfos
     * @param override 是否覆盖，是则清除原来的数据，再添加；否则追加数据
     */
    void updateNodeCache(List<NodeInfo> nodeInfos, boolean override, String chainId);

    /**
     * 指标信息
     */
    IndexInfo getIndexInfo(String chainId);
    void updateIndexCache(IndexInfo indexInfo, boolean override, String chainId);

    /**
     * 交易统计信息
     */
    StatisticInfo getStatisticInfo(String chainId);
    void updateStatisticCache(StatisticInfo statisticInfo, boolean override, String chainId);

    /**
     * 区块列表信息
     */
    BlockInit getBlockInit(String chainId);
    void updateBlockCache(List<BlockInfo> blockInfos, String chainId);

    /**
     * 交易列表信息
     */
    TransactionInit getTransactionInit(String chainId);
    void updateTransactionCache(List<TransactionInfo> transactionInfos, String chainId);

}
