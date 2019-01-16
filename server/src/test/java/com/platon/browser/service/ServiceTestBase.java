package com.platon.browser.service;

import com.platon.browser.TestBase;
import com.platon.browser.dao.entity.*;
import com.platon.browser.util.DataGenTool;
import org.junit.Assert;

import java.util.HashSet;
import java.util.List;

public class ServiceTestBase extends TestBase {

    protected void initNodeRankingTable(){
        chainsConfig.getChainIds().forEach(chainId -> {
            NodeRankingExample con = new NodeRankingExample();
            con.createCriteria().andChainIdEqualTo(chainId);
            nodeRankingMapper.deleteByExample(con);
            List<NodeRanking> data = DataGenTool.generateNode(chainId,false);

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
            List<Block> data = DataGenTool.generateBlock(chainId,false);

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
            List<TransactionWithBLOBs> data = DataGenTool.generateTransactionWithBLOB(chainId,false);

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
