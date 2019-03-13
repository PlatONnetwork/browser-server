package com.platon.browser.thread;

import com.github.pagehelper.PageHelper;
import com.platon.browser.bean.TransactionBean;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.dao.entity.*;
import com.platon.browser.dao.mapper.BlockMissingMapper;
import com.platon.browser.filter.BlockFilter;
import com.platon.browser.filter.TransactionFilter;
import com.platon.browser.service.DBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.Transaction;
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
public class AnalyseThread {
    private static Logger logger = LoggerFactory.getLogger(AnalyseThread.class);
    @Autowired
    private PlatonClient platon;
    @Value("${platon.chain.active}")
    private String chainId;
    @Value("${platon.block.pool.size}")
    private int threadNum;
    @Value("${platon.block.batch.num}")
    private int batchNum;
    @Autowired
    private BlockFilter blockFilter;
    @Autowired
    private TransactionFilter transactionFilter;
//    @Autowired
//    private NodeFilter nodeFilter;
    @Autowired
    private DBService dbService;
    @Autowired
    private BlockMissingMapper blockMissingMapper;

    public static ExecutorService THREAD_POOL;

    // 缺失块处理阈值，只有阈值达到才开启检查缺失块的处理逻辑
    private Integer threshold = 0;

    @PostConstruct
    private void init(){
         THREAD_POOL = Executors.newFixedThreadPool(threadNum);
    }

    public void analyse(List<EthBlock> blocks){
        if(blocks.size()==0) return;

        threshold++;

        List<AnalyseParam> params = new ArrayList<>();
        blocks.forEach(block->params.add(new AnalyseParam(block,platon.getWeb3j(chainId))));
        AnalyseResult result = new AnalyseResult();
//        Map<Long,List<NodeRanking>> nodeGroups = new HashMap<>();
        CountDownLatch latch = new CountDownLatch(params.size());
        params.forEach(param->
            THREAD_POOL.submit(()->{
                long threadTime = System.currentTimeMillis();
                try {
                    try {
                        Block block = blockFilter.analyse(param);
                        List<TransactionBean> transactions = transactionFilter.analyse(param, block.getTimestamp().getTime());
//                        List<NodeRanking> nodes = nodeFilter.analyse(param);
                        // 一切正常，则把分析结果添加到结果中
                        result.blocks.add(block);
                        result.transactions.addAll(transactions);
//                        nodeGroups.put(block.getNumber(),nodes);
                    } catch (Exception e) {
                        // 出错之后记录下出错的区块号，并返回
                        BlockMissing err = new BlockMissing();
                        err.setChainId(chainId);
                        err.setNumber(param.ethBlock.getBlock().getNumber().longValue());
                        result.errorBlocks.add(err);
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

        // 聚合所有区块的节点信息
//        result.nodes=nodeFilter.aggregateNodes(nodeGroups);

        // 分析完成后在同一事务中批量入库分析结果
        try {
            dbService.flush(result);
        } catch (Exception e) {
            List<Long> numbers = new ArrayList<>();
            result.blocks.forEach(block -> {
                numbers.add(block.getNumber());
                BlockMissing err = new BlockMissing();
                err.setChainId(chainId);
                err.setNumber(block.getNumber());
                result.errorBlocks.add(err);
            });
            if(result.errorBlocks.size()>0){
                BlockMissingExample example = new BlockMissingExample();
                example.createCriteria().andChainIdEqualTo(chainId).andNumberIn(numbers);
                blockMissingMapper.deleteByExample(example);
                blockMissingMapper.batchInsert(result.errorBlocks);
            }
        }

        // 处理缺失块，每批量采集五次，检测一下block_missing表里是否有缺失的块
        if(threshold%5==0){
            // 重置阈值，防止无限增长
            threshold=0;
            BlockMissingExample example = new BlockMissingExample();
            example.createCriteria().andChainIdEqualTo(chainId);
            example.setOrderByClause("number ASC");
            PageHelper.startPage(1,batchNum);
            List<BlockMissing> missings = blockMissingMapper.selectByExample(example);
            if(missings.size()==0) return;

            List<EthBlock> concurrentBlocks = new ArrayList<>();
            missings.forEach(missing->{
                try {
                    EthBlock ethBlock = platon.getWeb3j(chainId).ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(missing.getNumber())),true).send();
                    concurrentBlocks.add(ethBlock);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            List<Long> numbers = new ArrayList<>();
            if(concurrentBlocks.size()>0){
                // 删除block_missing表中实际从链上查询回来的块号
                concurrentBlocks.forEach(ethBlock->numbers.add(ethBlock.getBlock().getNumber().longValue()));
                example = new BlockMissingExample();
                example.createCriteria().andChainIdEqualTo(chainId).andNumberIn(numbers);
                blockMissingMapper.deleteByExample(example);
                // 分析缺失的块
                analyse(concurrentBlocks);
            }
        }

        if((System.currentTimeMillis()-startTime)>0) logger.debug("databaseService.flush(result): -> {}",System.currentTimeMillis()-startTime);
    }

    public static class AnalyseParam {
        public EthBlock ethBlock;
        public List <Transaction> transactions;
        public List <TransactionReceipt> transactionReceipts;
        public Map<String,Object> transactionReceiptMap;
        public AnalyseParam(EthBlock initData, Web3j web3j){
            this.ethBlock=initData;
            this.transactionReceiptMap = new HashMap <>();
            this.transactions = new ArrayList<>();
            this.transactionReceipts = new ArrayList <>();

            Map<String,Object> transactionMap = new HashMap<>();
            this.ethBlock.getBlock().getTransactions().forEach(transaction->{
                Transaction rawTransaction = (Transaction) transaction.get();
                transactions.add(rawTransaction);
                transactionMap.put(rawTransaction.getHash(),transaction);
                try {
                    EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(rawTransaction.getHash()).send();
                    Optional<TransactionReceipt> transactionReceipt = ethGetTransactionReceipt.getTransactionReceipt();
                    TransactionReceipt receipt = transactionReceipt.get();
                    transactionReceipts.add(receipt);
                    transactionReceiptMap.put(receipt.getTransactionHash(),receipt);
                } catch (IOException e) {
                    logger.error("Transaction resolve error: {}", e.getMessage());
                }
            });
        }
    }

    public static class AnalyseResult{
        public List<Block> blocks = new CopyOnWriteArrayList<>();
        public List<TransactionWithBLOBs> transactions = new CopyOnWriteArrayList<>();
        public List<BlockMissing> errorBlocks = new CopyOnWriteArrayList<>();
        public List<NodeRanking> nodes = new ArrayList<>();
    }
}
