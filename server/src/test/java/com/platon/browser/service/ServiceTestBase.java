package com.platon.browser.service;

import com.platon.browser.TestBase;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.util.TestDataUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ServiceTestBase extends TestBase {
    @Autowired
    private NodeRankingMapper nodeRankingMapper;
    @Autowired
    private BlockMapper blockMapper;
    @Autowired
    private TransactionMapper transactionMapper;

    protected void initNodeRankingTable(){
        chainsConfig.getChainIds().forEach(chainId -> {
            NodeRankingExample con = new NodeRankingExample();
            con.createCriteria().andChainIdEqualTo(chainId);
            nodeRankingMapper.deleteByExample(con);
            List<NodeRanking> data = TestDataUtil.generateNode(chainId);
            nodeRankingMapper.batchInsert(data);
        });
    }

    protected void initBlockTable(){
        chainsConfig.getChainIds().forEach(chainId -> {
            BlockExample con = new BlockExample();
            con.createCriteria().andChainIdEqualTo(chainId);
            blockMapper.deleteByExample(con);
            /*List<Block> data = TestDataUtil.generateBlock(chainId);
            blockMapper.batchInsert(data);*/
        });
    }

    protected void initTransactionTable(){
        chainsConfig.getChainIds().forEach(chainId -> {
            TransactionExample con = new TransactionExample();
            con.createCriteria().andChainIdEqualTo(chainId);
            transactionMapper.deleteByExample(con);
            List<TransactionWithBLOBs> data = TestDataUtil.generateTransactionWithBLOB(chainId);
            transactionMapper.batchInsert(data);
        });
    }
}
