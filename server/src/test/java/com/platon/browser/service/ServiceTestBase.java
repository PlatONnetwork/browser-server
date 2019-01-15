package com.platon.browser.service;

import com.platon.browser.TestBase;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.util.TestDataUtil;
import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;

public class ServiceTestBase extends TestBase {
    @Autowired
    protected NodeRankingMapper nodeRankingMapper;
    @Autowired
    protected BlockMapper blockMapper;
    @Autowired
    protected TransactionMapper transactionMapper;

    @Autowired
    protected BlockService blockService;
    @Autowired
    protected NodeService nodeService;
    @Autowired
    protected TransactionService transactionService;

    protected void initNodeRankingTable(){
        chainsConfig.getChainIds().forEach(chainId -> {
            NodeRankingExample con = new NodeRankingExample();
            con.createCriteria().andChainIdEqualTo(chainId);
            nodeRankingMapper.deleteByExample(con);
            List<NodeRanking> data = TestDataUtil.generateNode(chainId);

            if(data.size()==0){
                Assert.fail("No node data!");
                return;
            }

            nodeRankingMapper.batchInsert(data);

            nodeService.updatePushData(chainId,new HashSet<>(data));
        });
    }

    protected void initBlockTable(){
        chainsConfig.getChainIds().forEach(chainId -> {
            BlockExample con = new BlockExample();
            con.createCriteria().andChainIdEqualTo(chainId);
            blockMapper.deleteByExample(con);
            List<Block> data = TestDataUtil.generateBlock(chainId);

            if(data.size()==0){
                Assert.fail("No block data!");
                return;
            }

            blockMapper.batchInsert(data);

            blockService.clearCache(chainId);
            blockService.updateCache(chainId,new HashSet<>(data));
        });
    }

    protected void initTransactionTable(){
        chainsConfig.getChainIds().forEach(chainId -> {
            TransactionExample con = new TransactionExample();
            con.createCriteria().andChainIdEqualTo(chainId);
            transactionMapper.deleteByExample(con);
            List<TransactionWithBLOBs> data = TestDataUtil.generateTransactionWithBLOB(chainId);

            if(data.size()==0){
                Assert.fail("No transaction data!");
                return;
            }

            transactionMapper.batchInsert(data);

            transactionService.clearCache(chainId);
            transactionService.updateCache(chainId,new HashSet<>(data));
        });
    }
}
