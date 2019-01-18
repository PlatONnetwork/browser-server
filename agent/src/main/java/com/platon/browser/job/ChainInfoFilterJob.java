package com.platon.browser.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.github.pagehelper.PageHelper;
import com.platon.browser.client.Web3jClient;
import com.platon.browser.common.base.AppException;
import com.platon.browser.common.enums.ErrorCodeEnum;
import com.platon.browser.common.util.CalculatePublicKey;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.BlockExample;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.mapper.BlockMapper;
import com.platon.browser.filter.BlockCorrelationFlow;
import com.platon.browser.filter.OtherFlow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StopWatch;
import org.web3j.platon.contracts.CandidateContract;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import org.web3j.protocol.core.methods.response.*;

import javax.annotation.PostConstruct;
import javax.xml.crypto.Data;
import java.math.BigInteger;
import java.util.*;

/**
 * User: dongqile
 * Date: 2018/10/24
 * Time: 17:28
 */

public class ChainInfoFilterJob extends AbstractTaskJob {

    private static Logger log = LoggerFactory.getLogger(ChainInfoFilterJob.class);


    @Autowired
    private BlockMapper blockMapper;

    private static Long maxNubmer = 0L;

    @Value("${chain.id}")
    private String chainId;

    @Autowired
    private Web3jClient web3jClient;
    @Autowired
    private ChainsConfig chainsConfig;

    @Autowired
    private BlockCorrelationFlow blockCorrelationFlow;

    @Autowired
    private OtherFlow otherFlow;



    @PostConstruct
    public void init () {
        BlockExample condition = new BlockExample();
        condition.createCriteria().andChainIdEqualTo(chainId);
        condition.setOrderByClause("timestamp desc");
        PageHelper.startPage(1, 1);
        List <Block> blocks = blockMapper.selectByExample(condition);
        // 1、首先从数据库查询当前链的最高块号，作为采集起始块号
        // 2、如果查询不到则从0开始
        if (blocks.size() == 0) {
            maxNubmer = 0L;
        } else {
            maxNubmer = blocks.get(0).getNumber();
        }
    }

    @Override
       protected void doJob ( ShardingContext shardingContext ) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        log.debug("ChainInfoFilterJob-->{},begin data analysis!!!...");
        Map<String,Object> transactionMap = new HashMap <>();
        Map<String,Object> transactionReceiptMap = new HashMap <>();
        try {
            EthBlockNumber ethBlockNumber = null;
            Web3j web3j = chainsConfig.getWeb3j(chainId);
            try {
                ethBlockNumber = web3j.ethBlockNumber().send();
            } catch (Exception e) {
                log.error("Get blockNumber exception...", e);
                throw new AppException(ErrorCodeEnum.BLOCKCHAIN_ERROR);
            }
            String blockNumber = ethBlockNumber.getBlockNumber().toString();
             for (int i = maxNubmer.intValue() + 1; i <= Integer.parseInt(blockNumber); i++) {
                //select blockinfo from PlatON
                Date beginTime = new Date();
                DefaultBlockParameter defaultBlockParameter = new DefaultBlockParameterNumber(new BigInteger(String.valueOf(i)));
                EthBlock ethBlock = web3j.ethGetBlockByNumber(defaultBlockParameter, true).send();
                List <EthBlock.TransactionResult> list = ethBlock.getBlock().getTransactions();
                List <org.web3j.protocol.core.methods.response.Transaction> transactionList = new ArrayList <>();
                List <TransactionReceipt> transactionReceiptList = new ArrayList <>();
                for (EthBlock.TransactionResult transactionResult : list) {
                    org.web3j.protocol.core.methods.response.Transaction txList = (org.web3j.protocol.core.methods.response.Transaction) transactionResult.get();
                    EthTransaction ethTransaction = web3j.ethGetTransactionByHash(txList.getHash()).send();
                    Optional <org.web3j.protocol.core.methods.response.Transaction> value = ethTransaction.getTransaction();
                    org.web3j.protocol.core.methods.response.Transaction transaction = value.get();
                    transactionList.add(transaction);
                    transactionMap.put(transaction.getHash(),transaction);
                    EthGetTransactionReceipt ethGetTransactionReceipt = web3j.ethGetTransactionReceipt(transaction.getHash()).send();
                    Optional <TransactionReceipt> transactionReceipt = ethGetTransactionReceipt.getTransactionReceipt();
                    TransactionReceipt receipt = transactionReceipt.get();
                    transactionReceiptList.add(receipt);
                    transactionReceiptMap.put(receipt.getTransactionHash(),receipt);

                }
                //build candidate contract
                CandidateContract candidateContract = web3jClient.getCandidateContract();
                //get candidate list info
                EthPendingTransactions ethPendingTransactions = web3j.ethPendingTx().send();

                String nodeInfoList = null;
                try{
                    nodeInfoList = candidateContract.CandidateList(new BigInteger(String.valueOf(i))).send();
                }catch (Exception e){
                    log.debug("nodeInfoList is null !!!...",e.getMessage());
                }
                BigInteger publicKey = CalculatePublicKey.testBlock(ethBlock );
                Map<String,Object> map = blockCorrelationFlow.doFilter(ethBlock,transactionList,transactionReceiptList,publicKey,nodeInfoList,transactionReceiptMap);
                List<NodeRanking> nodeRankings = (List <NodeRanking>) map.get("nodeRanking");
                Block block = (Block) map.get("block");
                otherFlow.doFilter(ethPendingTransactions,nodeRankings,block);
                maxNubmer =Long.valueOf(blockNumber);
                Date endTime = new Date();
                String time = String.valueOf(endTime.getTime()-beginTime.getTime());
                log.info("--------------------------------------ChainInfoFilterJob :" + time);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            stopWatch.stop();
            log.debug("ChainInfoFilterJob-->{}", stopWatch.shortSummary());
        }

    }
}