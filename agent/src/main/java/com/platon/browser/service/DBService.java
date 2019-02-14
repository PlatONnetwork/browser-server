package com.platon.browser.service;

import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.BlockMissingExample;
import com.platon.browser.dao.mapper.*;
import com.platon.browser.thread.AnalyseThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
public class DBService {
    @Autowired
    private PlatonClient platon;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private TicketMapper ticketMapper;
    @Autowired
    private BlockMissingMapper blockMissingMapper;
    @Autowired
    protected RedisCacheService redisCacheService;
    @Autowired
    private CustomBlockMapper customBlockMapper;
//    @Autowired
//    private CustomNodeRankingMapper customNodeRankingMapper;

    @Transactional
    public void flush(AnalyseThread.AnalyseResult result){

        if(result.blocks.size()>0){
            blockMapper.batchInsert(result.blocks);
            // 更新区块中的节点名称字段：node_name
            customBlockMapper.updateBlockNodeName(platon.getChainId());
            redisCacheService.updateBlockCache(platon.getChainId(), new HashSet<>(result.blocks));
        }

        if(result.transactions.size()>0){
            transactionMapper.batchInsert(result.transactions);
            redisCacheService.updateTransactionCache(platon.getChainId(),new HashSet<>(result.transactions));
        }

        if(result.tickets.size()>0){
            ticketMapper.batchInsert(result.tickets);
        }

//        if(result.nodes.size()>0){
//            customNodeRankingMapper.insertOrUpdate(result.nodes);
//            redisCacheService.updateNodePushCache(platon.getChainId(), new HashSet<>(result.nodes));
//        }

        if(result.errorBlocks.size()>0) {
            // 先删后插，防止重复主键导致插入失败，以及防止因为重复主键错误导致整个事务回滚
            List<Long> numbers = new ArrayList<>();
            result.errorBlocks.forEach(err->numbers.add(err.getNumber()));
            BlockMissingExample example = new BlockMissingExample();
            example.createCriteria().andChainIdEqualTo(platon.getChainId()).andNumberIn(numbers);
            blockMissingMapper.deleteByExample(example);
            blockMissingMapper.batchInsert(result.errorBlocks);
        }
        redisCacheService.updateStatisticsCache(platon.getChainId());
    }
}
