package com.platon.browser.schedule;

import com.github.pagehelper.PageHelper;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.service.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class RedisCacheUpdateTask {

    private final Logger logger = LoggerFactory.getLogger(RedisCacheUpdateTask.class);

    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private TransactionMapper transactionMapper;

    /**
     * 更新Redis交易列表缓存
     */
    @Scheduled(cron="0/1 * * * * ?")
    public void updateTransactionTps(){
        chainsConfig.getChainIds().forEach(chainId -> {
            TransactionExample condition = new TransactionExample();
            condition.createCriteria().andChainIdEqualTo(chainId);
            condition.setOrderByClause("block_number desc,transaction_index desc");
            PageHelper.startPage(1,20);
            List<Transaction> transactionList = transactionMapper.selectByExample(condition);
            Set<Transaction> transactionSet = new HashSet<>();
            transactionList.forEach(transaction -> transactionSet.add(transaction));
            redisCacheService.updateTransactionCache(chainId,transactionSet);
        });
    }

}
