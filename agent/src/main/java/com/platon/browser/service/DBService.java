package com.platon.browser.service;

import com.platon.browser.dao.entity.Transaction;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.BlockMissingMapper;
import com.platon.browser.dao.mapper.CustomBlockMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.thread.AnalyseThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class DBService {

    @Value("${chain.id}")
    private String chainId;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private BlockMissingMapper blockMissingMapper;
    @Autowired
    protected RedisCacheService redisCacheService;
    @Autowired
    private CustomBlockMapper customBlockMapper;

    @Transactional
    public void flush(AnalyseThread.AnalyseResult result){

        if(result.blocks.size()>0){
            blockMapper.batchInsert(result.blocks);
            // 更新区块中的节点名称字段：node_name
            customBlockMapper.updateBlockNodeName(chainId);
            // 更新缓存
            redisCacheService.updateBlockCache(chainId, new HashSet<>(result.blocks));
        }

        if(result.transactions.size()>0){
            transactionMapper.batchInsert(result.transactions);
            // 更新缓存
            List<String> txHashes = new ArrayList<>();
            result.transactions.forEach(transaction->txHashes.add(transaction.getHash()));
            TransactionExample condition = new TransactionExample();
            condition.createCriteria().andChainIdEqualTo(chainId).andHashIn(txHashes);
            List<Transaction> dbTrans = transactionMapper.selectByExample(condition);
            redisCacheService.updateTransactionCache(chainId,new HashSet<>(dbTrans));
        }

        if(result.errorBlocks.size()>0){
            blockMissingMapper.batchInsert(result.errorBlocks);
        }

        // 更新统计缓存
        redisCacheService.updateStatisticsCache(chainId);
    }
}
