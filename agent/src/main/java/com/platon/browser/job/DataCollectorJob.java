package com.platon.browser.job;

import com.github.pagehelper.PageHelper;
import com.platon.browser.client.Web3jClient;
import com.platon.browser.common.util.CalculatePublicKey;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.filter.BlockCorrelationFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.platon.contracts.CandidateContract;
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
    private Web3jClient web3jClient;
    @Autowired
    private ChainsConfig chainsConfig;

    @Autowired
    private BlockCorrelationFlow blockCorrelationFlow;

    private long beginNumber=1;
    private boolean isPrevDone = true;

    private Web3j web3j;

    public static class AnalysisParam {
        public EthBlock ethBlock;
        public List <org.web3j.protocol.core.methods.response.Transaction> transactionList;
        public List <TransactionReceipt> transactionReceiptList;
        public Map<String,Object> transactionReceiptMap;
        public BigInteger publicKey;
        public String nodeInfoList;
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
        if (isPrevDone){
            logger.debug("In the job **************************");
            isPrevDone = false;
            try {
                BigInteger endNumber = web3j.ethBlockNumber().send().getBlockNumber();
                while (beginNumber<=endNumber.longValue()){
                    EthBlock ethBlock = web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(beginNumber)),true).send();
                    analysis(ethBlock);
                    beginNumber++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            isPrevDone=true;
        }
    }

    private void analysis(EthBlock ethBlock) throws Exception {
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
                EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(rawTransaction.getHash()).send();
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

        try{
            CandidateContract candidateContract = web3jClient.getCandidateContract();
            param.nodeInfoList=candidateContract.CandidateList(ethBlock.getBlock().getNumber()).send();
        }catch (Exception e){
            logger.debug("nodeInfoList is null !!!...",e.getMessage());
        }

        try {
            param.publicKey = CalculatePublicKey.testBlock(ethBlock );
        } catch (Exception e) {
            logger.debug("Public key is null !!!...",e.getMessage());
        }

        try {
            blockCorrelationFlow.doFilter(param);
        } catch (Exception e) {
            logger.error("Invoke blockCorrelationFlow.doFilter() error: {}", e.getMessage());
        }
    }
}