package com.platon.browser.service;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.StatisticPushItem;
import com.platon.browser.dto.StatisticsCache;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.block.BlockPushItem;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.dto.transaction.TransactionPushItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface RedisCacheService {
    void clearBlockCache(String chainId);
    void updateBlockCache(String chainId, Set<Block> items);
    void resetBlockCache(String chainId, boolean clearOld);
    void clearTransactionCache(String chainId);
    void clearStatisticsCache(String chainId);
    void updateTransactionCache(String chainId, Set<Transaction> items);
    void resetTransactionCache(String chainId, boolean clearOld);
    RespPage<BlockListItem> getBlockPage(String chainId, int pageNum, int pageSize);
    List<BlockPushItem> getBlockPushCache(String chainId, int pageNum, int pageSize);
    RespPage<TransactionListItem> getTransactionPage(String chainId, int pageNum, int pageSize);
    List<TransactionPushItem> getTransactionPushCache(String chainId, int pageNum, int pageSize);
    void clearNodePushCache(String chainId);
    void updateNodePushCache(String chainId, Set<NodeRanking> items);
    void resetNodePushCache(String chainId, boolean clearOld);
    List<NodePushItem> getNodePushCache(String chainId);
    List<StatisticPushItem> getStatisticPushCache(String chainId, int pageNum, int pageSize);
    boolean updateStatisticsCache(String chainId);
    StatisticsCache getStatisticsCache(String chainId);
    boolean updateTransCount(String chainId);
    long getTransCount(String chainId);
    boolean updateTransCount24Hours(String chainId);
    long getTransCount24Hours(String chainId);
    boolean updateAddressCount(String chainId);
    long getAddressCount(String chainId);
    boolean updateAvgBlockTransCount(String chainId);
    BigDecimal getAvgBlockTransCount(String chainId);
    boolean updateTicketPrice(String chainId);
    BigDecimal getTicketPrice(String chainId);
    boolean updateVoteCount(String chainId);
    long getVoteCount(String chainId);
}
