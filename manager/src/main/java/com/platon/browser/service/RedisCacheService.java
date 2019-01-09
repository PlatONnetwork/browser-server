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
    void updateTransactionCount(String chainId, int step);
    void updateBlockCache(String chainId, Set<Block> items);
    void updateTransactionCache(String chainId, Set<Transaction> items);
    RespPage<BlockListItem> getBlockPage(String chainId, int pageNum, int pageSize);
    List<BlockPushItem> getBlockPushData(String chainId, int pageNum, int pageSize);
    RespPage<TransactionListItem> getTransactionPage(String chainId, int pageNum, int pageSize);
    List<TransactionPushItem> getTransactionPushData(String chainId, int pageNum, int pageSize);
    List<NodePushItem> getNodeList(String chainId);
    void updateNodeCache(String chainId, Set<NodeRanking> items);
    List<StatisticPushItem> getStatisticPushData(String chainId, int pageNum, int pageSize);
}
