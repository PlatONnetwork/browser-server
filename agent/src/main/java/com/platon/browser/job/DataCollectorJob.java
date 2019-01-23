package com.platon.browser.job;

import com.github.pagehelper.PageHelper;
import com.platon.browser.common.util.CalculatePublicKey;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.NodeRankingExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.dao.mapper.NodeRankingMapper;
import com.platon.browser.filter.BlockCorrelationFlow;
import com.platon.browser.filter.BlockFilter;
import com.platon.browser.filter.CacheTool;
import com.platon.browser.service.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

/**
 * User: dongqile
 * Date: 2018/10/24
 * Time: 17:28
 */
@Component
public class DataCollectorJob {

    private static Logger logger = LoggerFactory.getLogger(DataCollectorJob.class);

    @Autowired
    private BlockMapper blockMapper;

    @Value("${chain.id}")
    private String chainId;

    @Autowired
    private ChainsConfig chainsConfig;

    @Autowired
    private BlockCorrelationFlow blockCorrelationFlow;

    @Autowired
    private BlockFilter blockFilter;

    private long beginNumber=1;

    private Web3j web3j;

    @Autowired
    private NodeRankingMapper nodeRankingMapper;

    public final static Map<String,String> NODE_ID_TO_NAME = new HashMap<>();

    @Autowired
    private RedisCacheService redisCacheService;

    public static class AnalysisParam {
        public EthBlock ethBlock;
        public List <org.web3j.protocol.core.methods.response.Transaction> transactionList;
        public List <TransactionReceipt> transactionReceiptList;
        public Map<String,Object> transactionReceiptMap;
        public BigInteger publicKey;
    }

    @PostConstruct
    public void init () {
        BlockExample condition = new BlockExample();
        condition.createCriteria().andChainIdEqualTo(chainId);
        condition.setOrderByClause("number desc");
        PageHelper.startPage(1, 1);
        List <Block> blocks = blockMapper.selectByExample(condition);
        // 1、首先从数据库查询当前链的最高块号，作为采集起始块号
        // 2、如果查询不到则从0开始
        if (blocks.size() == 0) {
            beginNumber = 1L;
        } else {
            beginNumber = blocks.get(0).getNumber()+1;
        }
        web3j = chainsConfig.getWeb3j(chainId);
    }

    @Scheduled(cron="0/1 * * * * ?")
    protected void doJob () {
        try {
            NodeRankingExample nodeRankingExample = new NodeRankingExample();
            nodeRankingExample.createCriteria().andChainIdEqualTo(chainId).andIsValidEqualTo(1);
            List <NodeRanking> nodes = nodeRankingMapper.selectByExample(nodeRankingExample);
            NODE_ID_TO_NAME.clear();
            nodes.forEach(node->NODE_ID_TO_NAME.put(node.getNodeId(),node.getName()));

            BigInteger endNumber = web3j.ethBlockNumber().send().getBlockNumber();
            while (beginNumber<=endNumber.longValue()){
                long startTime = System.currentTimeMillis();
                EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(beginNumber)),true).send();
                logger.debug("RPC web3j.ethGetBlockByNumber()--->{}",System.currentTimeMillis()-startTime);
                analysis(ethBlock);
                beginNumber++;
            }
            if(CacheTool.currentBlockNumber==endNumber.longValue()){
                // 如果本地缓存中的当前块高等于本次从链上取回来的最高块高，则把本地缓存中的缓存刷入数据库和redis
                blockFilter.flush();
            }
        } catch (Exception e) {
            // 出现异常，需要把本地缓存刷入数据库和redis
            blockFilter.flush();
            e.printStackTrace();
        }
    }

    private void analysis(EthBlock ethBlock) {

        logger.debug("************** Analysis start ***************");

        long startTime1 = System.currentTimeMillis();

        // 构造分析参数
        AnalysisParam param = new AnalysisParam();
        param.ethBlock = ethBlock;

        Map<String,Object> transactionMap = new HashMap <>();
        Map<String,Object> transactionReceiptMap = new HashMap <>();

        List <EthBlock.TransactionResult> transactions = ethBlock.getBlock().getTransactions();

        List <org.web3j.protocol.core.methods.response.Transaction> transactionList = new ArrayList<>();
        List <TransactionReceipt> transactionReceiptList = new ArrayList <>();
        transactions.forEach(transaction->{
            org.web3j.protocol.core.methods.response.Transaction rawTransaction = (org.web3j.protocol.core.methods.response.Transaction) transaction.get();
            transactionList.add(rawTransaction);
            transactionMap.put(rawTransaction.getHash(),transaction);
            try {
                long startTime = System.currentTimeMillis();
                EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(rawTransaction.getHash()).send();
                logger.debug("RPC web3j.ethGetTransactionReceipt()--->{}",System.currentTimeMillis()-startTime);
                Optional<TransactionReceipt> transactionReceipt = ethGetTransactionReceipt.getTransactionReceipt();
                TransactionReceipt receipt = transactionReceipt.get();
                transactionReceiptList.add(receipt);
                transactionReceiptMap.put(receipt.getTransactionHash(),receipt);
            } catch (IOException e) {
                logger.error("Transaction resolve error: {}", e.getMessage());
            }
        });

        param.transactionList=transactionList;
        param.transactionReceiptList=transactionReceiptList;
        param.transactionReceiptMap=transactionReceiptMap;

        long startTime4 = System.currentTimeMillis();
        try {
            param.publicKey = CalculatePublicKey.testBlock(ethBlock);
//            param.publicKey = BigInteger.TEN;
        } catch (Exception e) {
            logger.debug("Public key is null !!!...",e.getMessage());
        }
        logger.debug("CalculatePublicKey.testBlock()         :--->{}",System.currentTimeMillis()-startTime4);

        try {
            long startTime2 = System.currentTimeMillis();
            blockCorrelationFlow.doFilter(param);
            logger.debug("BlockCorrelationFlow.doFilter(param):--->{}",System.currentTimeMillis()-startTime2);
        } catch (Exception e) {
            logger.error("Invoke blockCorrelationFlow.doFilter() error: {}", e.getMessage());
        }

        logger.debug("DataCollectorJob.analysis()         :--->{}",System.currentTimeMillis()-startTime1);

        logger.debug("************** Analysis finished ***************");
    }
}