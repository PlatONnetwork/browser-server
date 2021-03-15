package com.platon.browser.utils;
/*
package com.platon.browser.util;

import com.github.pagehelper.PageHelper;
import com.platon.browser.TestBase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
public class DatabaseTool extends TestBase {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseTool.class);
    private static final int pageNum = 1;
    private static final int pageSize = 100;

    */
/**
     * 导出测试数据
     *//*

    @Test
    public void exportData() {
        chainsConfig.getChainIds().forEach(chainId->{
            */
/*BlockExample blockExample = new BlockExample();
            blockExample.createCriteria().andChainIdEqualTo(chainId);
            PageHelper.startPage(pageNum,pageSize);
            List<Block> blocks = blockMapper.selectByExample(blockExample);
            DataGenTool.writeToFile(chainId,TestDataFileNameEnum.BLOCK,blocks);*//*


            NodeRankingExample nodeRankingExample = new NodeRankingExample();
            nodeRankingExample.createCriteria().andChainIdEqualTo(chainId);
            PageHelper.startPage(pageNum,pageSize);
            List<NodeRanking> nodes = nodeRankingMapper.selectByExample(nodeRankingExample);
            DataGenTool.writeToFile(chainId, TestDataFileNameEnum.NODE,nodes);

            */
/*TransactionExample transactionExample = new TransactionExample();
            transactionExample.createCriteria().andChainIdEqualTo(chainId);
            PageHelper.startPage(pageNum,pageSize);
            List<TransactionWithBLOBs> transactions = transactionMapper.selectByExampleWithBLOBs(transactionExample);
            DataGenTool.writeToFile(chainId,TestDataFileNameEnum.TRANSACTION,transactions);

            PendingTxExample pendingTxExample = new PendingTxExample();
            pendingTxExample.createCriteria().andChainIdEqualTo(chainId);
            PageHelper.startPage(pageNum,pageSize);
            List<PendingTx> pendingTxes = pendingTxMapper.selectByExampleWithBLOBs(pendingTxExample);
            DataGenTool.writeToFile(chainId,TestDataFileNameEnum.PENDINGTX,pendingTxes);*//*

        });
    }

    */
/**
     * 导入测试数据
     *//*

    @Test
    public void importData() {
        chainsConfig.getChainIds().forEach(chainId->{
            try {
                */
/*BlockExample blockExample = new BlockExample();
                blockExample.createCriteria().andChainIdEqualTo(chainId);
                blockMapper.deleteByExample(blockExample);
                List<Block> blocks = DataGenTool.getTestData(chainId,TestDataFileNameEnum.BLOCK,Block.class);
                if(blocks.size()>0) blockMapper.batchInsert(blocks);

                NodeRankingExample nodeRankingExample = new NodeRankingExample();
                nodeRankingExample.createCriteria().andChainIdEqualTo(chainId);
                nodeRankingMapper.deleteByExample(nodeRankingExample);
                List<NodeRanking> nodes = DataGenTool.getTestData(chainId,TestDataFileNameEnum.NODE,NodeRanking.class);
                if(nodes.size()>0) nodeRankingMapper.batchInsert(nodes);

                TransactionExample transactionExample = new TransactionExample();
                transactionExample.createCriteria().andChainIdEqualTo(chainId);
                transactionMapper.deleteByExample(transactionExample);
                List<TransactionWithBLOBs> transactions = DataGenTool.getTestData(chainId,TestDataFileNameEnum.TRANSACTION,TransactionWithBLOBs.class);
                if(transactions.size()>0) transactionMapper.batchInsert(transactions);*//*


                PendingTxExample pendingTxExample = new PendingTxExample();
                pendingTxExample.createCriteria().andChainIdEqualTo(chainId);
                pendingTxMapper.deleteByExample(pendingTxExample);
                List<PendingTx> pendingTxes = DataGenTool.getTestData(chainId, TestDataFileNameEnum.PENDINGTX, PendingTx.class);
                if(pendingTxes.size()>0) pendingTxMapper.batchInsert(pendingTxes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}
*/
