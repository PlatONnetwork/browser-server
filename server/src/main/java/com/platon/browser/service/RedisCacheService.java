package com.platon.browser.service;

import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.block.BlockItem;
import com.platon.browser.dto.transaction.TransactionItem;

import java.util.Set;

public interface RedisCacheService {
    void updateBlockCount(String chainId, int step);
    void updateTransactionCount(String chainId, int step);
    void updateBlockCache(String chainId, Set<Block> items);
    void updateTransactionCache(String chainId, Set<Transaction> items);
    RespPage<BlockItem> getBlockPage(String chainId, int pageNum, int pageSize);
    RespPage<TransactionItem> getTransactionPage(String chainId, int pageNum, int pageSize);
}
