package com.platon.browser.service.cache;

import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.dto.transaction.TransactionPushItem;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TransactionCacheService {
    void clearTransactionCache(String chainId);
    void updateTransactionCache(String chainId, Set<Transaction> items);
    void resetTransactionCache(String chainId, boolean clearOld);
    RespPage<TransactionListItem> getTransactionPage(String chainId, int pageNum, int pageSize);
    List<TransactionPushItem> getTransactionPushCache(String chainId, int pageNum, int pageSize);
    void classifyByAddress(String chainId, List<TransactionWithBLOBs> transactions );
    void retentionValidData(String address,String chainId);
    Collection<TransactionWithBLOBs> fuzzyQuery(String chainId, String addressPattern, String txTypePattern, String txHashPattern, String timestampPattern);
}
