package com.platon.browser.service;

import com.platon.browser.TestBase;
import com.platon.browser.dao.entity.*;
import com.platon.browser.util.DataTool;
import org.junit.Assert;

import java.util.HashSet;
import java.util.List;

public class ServiceTestBase extends TestBase {

    /**
     * 初始化节点表及推送缓存
     */
    protected void initNodeRankingTableAndCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            NodeRankingExample con = new NodeRankingExample();
            con.createCriteria().andChainIdEqualTo(chainId);
            nodeRankingMapper.deleteByExample(con);
            List<NodeRanking> data = DataTool.getTestData(chainId, TestDataFileNameEnum.NODE, NodeRanking.class);
            if(data.size()==0){
                Assert.fail("No node data!");
                return;
            }
            nodeRankingMapper.batchInsert(data);
            nodeService.clearPushCache(chainId);
            nodeService.updatePushData(chainId,new HashSet<>(data));
        });
    }

    /**
     * 初始化区块表及缓存
     */
    protected void initBlockTableAndCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            BlockExample con = new BlockExample();
            con.createCriteria().andChainIdEqualTo(chainId);
            blockMapper.deleteByExample(con);
            List<Block> data = DataTool.getTestData(chainId, TestDataFileNameEnum.BLOCK, Block.class);
            if(data.size()==0){
                Assert.fail("No block data!");
                return;
            }
            blockMapper.batchInsert(data);
            blockService.clearCache(chainId);
            blockService.updateCache(chainId,new HashSet<>(data));
        });
    }
    /**
     * 初始化交易表及缓存
     */
    protected void initTransactionTableAndCache(){
        chainsConfig.getChainIds().forEach(chainId -> {
            TransactionExample con = new TransactionExample();
            con.createCriteria().andChainIdEqualTo(chainId);
            transactionMapper.deleteByExample(con);
            List<TransactionWithBLOBs> data = DataTool.getTestData(chainId, TestDataFileNameEnum.TRANSACTION, TransactionWithBLOBs.class);
            if(data.size()==0){
                Assert.fail("No transaction data!");
                return;
            }
            transactionMapper.batchInsert(data);
            transactionService.clearCache(chainId);
            transactionService.updateCache(chainId,new HashSet<>(data));
        });
    }

    /**
     * 初始化交易表及缓存
     */
    protected void initPendingTxTable(){
        chainsConfig.getChainIds().forEach(chainId -> {
            PendingTxExample con = new PendingTxExample();
            con.createCriteria().andChainIdEqualTo(chainId);
            pendingTxMapper.deleteByExample(con);
            List<PendingTx> data = DataTool.getTestData(chainId, TestDataFileNameEnum.PENDINGTX, PendingTx.class);
            if(data.size()==0){
                Assert.fail("No PendingTx data!");
                return;
            }
            pendingTxMapper.batchInsert(data);
        });
    }
}
