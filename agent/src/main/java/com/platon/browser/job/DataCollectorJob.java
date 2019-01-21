package com.platon.browser.job;

import com.github.pagehelper.PageHelper;
import com.platon.browser.client.Web3jClient;
import com.platon.browser.common.util.CalculatePublicKey;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.filter.BlockCorrelationFlow;
import com.platon.browser.filter.CacheTool;
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
import rx.Subscription;

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

    private boolean isError = true;

    public static class AnalysisParam {
        public EthBlock ethBlock;
        public List <org.web3j.protocol.core.methods.response.Transaction> transactionList;
        public List <TransactionReceipt> transactionReceiptList;
        public Map<String,Object> transactionReceiptMap;
        public BigInteger publicKey;
        public String nodeInfoList;
    }

    private Subscription subscription;

    @Scheduled(cron="0/1 * * * * ?")
    protected void doJob () {

        if (isError){
            logger.debug("In the job **************************");
            isError = false;
            BlockExample condition = new BlockExample();
            condition.createCriteria().andChainIdEqualTo(chainId);
            condition.setOrderByClause("number desc");
            PageHelper.startPage(1, 1);
            List <Block> blocks = blockMapper.selectByExample(condition);
            long beginNumber = 1;
            if (blocks.size() > 0) {
                beginNumber = blocks.get(0).getNumber()+1;
            }

            Web3j web3j = chainsConfig.getWeb3j(chainId);
            subscription = web3j.catchUpToLatestAndSubscribeToNewBlocksObservable(DefaultBlockParameter.valueOf(BigInteger.valueOf(beginNumber)),true)
                .doOnError(ex->{
                    // 取消订阅
                    if(subscription!=null) subscription.unsubscribe();
                    // 设置为true，等待下一秒定时任务进来重新订阅
                    this.isError=true;
                })
                .subscribe(ethBlock -> {

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

                    try {
                        // 如果当前链上块高等于分析后的块高，证明已追上，把剩余的缓存块批量入库
                        long chainCurrentBlockNumber = web3j.ethBlockNumber().send().getBlockNumber().longValue();
                        if(CacheTool.currentBlockNumber==chainCurrentBlockNumber){
                            blockMapper.batchInsert(CacheTool.blocks);
                            CacheTool.blocks.clear();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        }
    }
}