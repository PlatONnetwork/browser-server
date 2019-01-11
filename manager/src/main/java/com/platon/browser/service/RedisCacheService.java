package com.platon.browser.service;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.StatisticPushItem;
import com.platon.browser.dto.block.BlockPushItem;
import com.platon.browser.dto.block.BlockListItem;
import com.platon.browser.dto.node.NodePushItem;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.dto.transaction.TransactionPushItem;

import java.util.List;
import java.util.Set;

public interface RedisCacheService {
    void clearBlockCache(String chainId);
    void updateBlockCache(String chainId, Set<Block> items);
    void clearTransactionCache(String chainId);
    void updateTransactionCache(String chainId, Set<Transaction> items);
    RespPage<BlockListItem> getBlockPage(String chainId, int pageNum, int pageSize);
    List<BlockPushItem> getBlockPushCache(String chainId, int pageNum, int pageSize);
    RespPage<TransactionListItem> getTransactionPage(String chainId, int pageNum, int pageSize);
    List<TransactionPushItem> getTransactionPushCache(String chainId, int pageNum, int pageSize);
    void clearNodePushCache(String chainId);
    List<NodePushItem> getNodePushCache(String chainId);
    void updateNodePushCache(String chainId, Set<NodeRanking> items);
    List<StatisticPushItem> getStatisticPushCache(String chainId, int pageNum, int pageSize);
}
