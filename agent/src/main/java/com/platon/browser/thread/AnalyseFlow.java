package com.platon.browser.thread;

import com.platon.browser.common.util.CalculatePublicKey;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockMissing;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.filter.BlockFilter;
import com.platon.browser.filter.TransactionFilter;
import com.platon.browser.service.DatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class AnalyseFlow {
    private static Logger logger = LoggerFactory.getLogger(AnalyseFlow.class);
    public static class AnalysisParam {
        public EthBlock ethBlock;
        public List <org.web3j.protocol.core.methods.response.Transaction> transactionList;
        public List <TransactionReceipt> transactionReceiptList;
        public Map<String,Object> transactionReceiptMap;
        public BigInteger publicKey;
    }
    public static class AnalysisResult{
        public List<Block> blocks = new CopyOnWriteArrayList<>();
        public List<TransactionWithBLOBs> transactions = new CopyOnWriteArrayList<>();
        public List<BlockMissing> errorBlocks = new CopyOnWriteArrayList<>();
        public List<NodeRanking> nodes = new CopyOnWriteArrayList<>();
    }

    @Value("${chain.id}")
    private String chainId;
    @Value("${platon.thread.batch.size}")
    private int threadBatchSize;
    @Autowired
    private ChainsConfig chainsConfig;
    @Autowired
    private BlockFilter blockFilter;
    @Autowired
    private TransactionFilter transactionFilter;
    @Autowired
    private DatabaseService databaseService;

    public static ExecutorService THREAD_POOL;
    @PostConstruct
    private void init(){
         THREAD_POOL = Executors.newFixedThreadPool(threadBatchSize);
    }

    public void analyse(List<EthBlock> blocks){
        List<AnalysisParam> params = new ArrayList<>();
        blocks.forEach(block->params.add(getAnalyseParam(block)));
        AnalysisResult result = new AnalysisResult();
        CountDownLatch latch = new CountDownLatch(params.size());
        params.forEach(param->
            THREAD_POOL.submit(()->{
                long threadTime = System.currentTimeMillis();
                try {
                    try {
                        Block block = blockFilter.analysis(param);
                        List<TransactionWithBLOBs> transactions = transactionFilter.analysis(param, block.getTimestamp().getTime());
                        // 一切正常，则把分析结果添加到结果中
                        result.blocks.add(block);
                        result.transactions.addAll(transactions);
                    } catch (Exception e) {
                        // 出错之后记录下出错的区块号，并返回
                        BlockMissing err = new BlockMissing();
                        err.setNumber(param.ethBlock.getBlock().getNumber().longValue());
                        result.errorBlocks.add(err);
                        return;
                    }
                }finally {
                    latch.countDown();
                }
                if((System.currentTimeMillis()-threadTime)>0) logger.debug("Thread ID={},NAME={} : -> {}",Thread.currentThread().getId(),Thread.currentThread().getName(),System.currentTimeMillis()-threadTime);
            })
        );

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long startTime = System.currentTimeMillis();
        // 分析完成后在同一事务中批量入库分析结果
        databaseService.flush(result);
        if((System.currentTimeMillis()-startTime)>0) logger.debug("databaseService.flush(result): -> {}",System.currentTimeMillis()-startTime);
    }

    /**
     * 通过原生区块信息构造分析参数
     * @param ethBlock
     * @return
     */
    private AnalysisParam getAnalyseParam(EthBlock ethBlock) {

        logger.debug("************** Analysis start ***************");

        long startTime = System.currentTimeMillis();

        // 构造分析参数
        AnalysisParam param = new AnalysisParam();
        param.ethBlock = ethBlock;

        Map<String,Object> transactionMap = new HashMap<>();
        Map<String,Object> transactionReceiptMap = new HashMap <>();

        List <EthBlock.TransactionResult> transactions = ethBlock.getBlock().getTransactions();

        List <org.web3j.protocol.core.methods.response.Transaction> transactionList = new ArrayList<>();
        List <TransactionReceipt> transactionReceiptList = new ArrayList <>();
        transactions.forEach(transaction->{
            org.web3j.protocol.core.methods.response.Transaction rawTransaction = (org.web3j.protocol.core.methods.response.Transaction) transaction.get();
            transactionList.add(rawTransaction);
            transactionMap.put(rawTransaction.getHash(),transaction);
            try {
                EthGetTransactionReceipt ethGetTransactionReceipt = chainsConfig.getWeb3j(chainId).ethGetTransactionReceipt(rawTransaction.getHash()).send();
                Optional<TransactionReceipt> transactionReceipt = ethGetTransactionReceipt.getTransactionReceipt();
                TransactionReceipt receipt = transactionReceipt.get();
                transactionReceiptList.add(receipt);
                transactionReceiptMap.put(receipt.getTransactionHash(),receipt);
            } catch (IOException e) {
                logger.error("Transaction resolve error: {}", e.getMessage());
            }
        });
        logger.debug("ethGetTransactionReceipt()         :--->{}",System.currentTimeMillis()-startTime);

        param.transactionList=transactionList;
        param.transactionReceiptList=transactionReceiptList;
        param.transactionReceiptMap=transactionReceiptMap;

        startTime = System.currentTimeMillis();
        try {
            param.publicKey = CalculatePublicKey.testBlock(ethBlock);
        } catch (Exception e) {
            logger.debug("Public key is null !!!...",e.getMessage());
        }
        logger.debug("CalculatePublicKey.testBlock()         :--->{}",System.currentTimeMillis()-startTime);

        return param;
    }
}
