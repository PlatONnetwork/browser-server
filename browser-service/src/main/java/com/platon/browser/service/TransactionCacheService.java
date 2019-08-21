package com.platon.browser.service;

import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.RespPage;
import com.platon.browser.dto.transaction.TransactionListItem;
import com.platon.browser.dto.transaction.TransactionPushItem;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TransactionCacheService {
    void clear();
    void update(Set<Transaction> items);
    void reset(boolean clearOld);
}
