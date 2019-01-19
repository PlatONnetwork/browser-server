package com.platon.browser.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.platon.browser.common.base.AppException;
import com.platon.browser.common.dto.agent.CandidateDto;
import com.platon.browser.common.enums.ErrorCodeEnum;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.NodeRanking;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.service.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.EthPendingTransactions;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: dongqile
 * Date: 2019/1/15
 * Time: 14:34
 */
@Component
public class BlockCorrelationFlow {

    private static Logger log = LoggerFactory.getLogger(BlockCorrelationFlow.class);

    @Value("${chain.id}")
    private String chainId;

    @Autowired
    private BlockFilter blockFilter;

    @Autowired
    private TransactionFilter transactionFilter;

    @Autowired
    private NodeFilter nodeFilter;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private OtherFlow otherFlow;
    @Autowired
    private ChainsConfig chainsConfig;

    private final static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void doFilter (EthBlock ethBlock, List<Transaction> transactionList,List<TransactionReceipt> transactionReceiptList,
                                       BigInteger publicKey,String nodeInfoList,Map<String,Object> transactionReceiptMap) throws Exception {
        Block block = null;
        try {
            block = blockFilter.blockAnalysis(ethBlock, transactionReceiptList, transactionList,nodeInfoList,publicKey,transactionReceiptMap);
            Set <Block> set = new HashSet <>(CacheTool.blocksCache);
            CacheTool.blocksCache.clear();
            executorService.submit(()->redisCacheService.updateBlockCache(chainId, set));
            String blockString = JSON.toJSONString(block);
            log.debug("block info :" + blockString);
            if (StringUtils.isEmpty(block)) {
                log.error("Analysis Block is null !!!....");
            }
        } catch (Exception e) {
            log.error("Block Filter exception", e);
            log.error("Block analysis exception", e.getMessage());
            throw new AppException(ErrorCodeEnum.BLOCKCHAIN_ERROR);
        }

        if(block==null) block = new Block();
        Block blockRef = block;

        executorService.submit(()->{
            log.debug("Transaction thread[name:{},id:{}] ",Thread.currentThread().getName(),Thread.currentThread().getId());
            try {
                if (transactionList.size() > 0 && transactionReceiptList.size() > 0
                        && transactionList != null && transactionReceiptList != null) {
                    List<String> txHash = transactionFilter.transactionAnalysis(transactionReceiptMap, transactionList, blockRef.getTimestamp().getTime());
                    if (txHash.size()>0) {
                        executorService.submit(()->{
                            log.debug("Redis update thread[name:{},id:{}] ",Thread.currentThread().getName(),Thread.currentThread().getId());
                            TransactionExample condition = new TransactionExample();
                            condition.createCriteria().andChainIdEqualTo(chainId).andHashIn(txHash);
                            List<com.platon.browser.dao.entity.Transaction> dbTrans = transactionMapper.selectByExample(condition);
                            redisCacheService.updateTransactionCache(chainId,new HashSet <>(dbTrans));
                        });
                    }
                }
            } catch (Exception e) {
                log.error("Transaction Filter exception", e);
                log.error("Transaction analysis exception", e.getMessage());
                throw new AppException(ErrorCodeEnum.TX_ERROR);
            }
        });

        executorService.submit(()->{
            try {
                List <CandidateDto> list = JSON.parseArray(nodeInfoList, CandidateDto.class);
                if (!StringUtils.isEmpty(nodeInfoList) && list.size() > 0) {
                    List<NodeRanking> nodeRankings = nodeFilter.nodeAnalysis(nodeInfoList, blockRef.getNumber().longValue(), ethBlock, blockRef.getBlockReward(),publicKey);

                    executorService.submit(()->{
                        log.debug("Redis update thread[name:{},id:{}] ",Thread.currentThread().getName(),Thread.currentThread().getId());
                        Set <NodeRanking> nodes = new HashSet <>(nodeRankings);
                        redisCacheService.updateNodePushCache(chainId, nodes);
                    });

                    String nodeRankingString = JSONArray.toJSONString(nodeRankings);
                    log.debug("node info :" + nodeRankingString);
                    if (nodeRankings.size() < 0 && nodeRankings == null) {
                        log.error("Analysis NodeInfo is null !!!....");
                    }

                    executorService.submit(()->{
                        try {
                            EthPendingTransactions ethPendingTransactions = chainsConfig.getWeb3j(chainId).ethPendingTx().send();
                            otherFlow.doFilter(ethPendingTransactions,nodeRankings,blockRef);
                        } catch (IOException e) {
                            log.error("OtherFlow execute error !!!....");
                        }

                    });
                }
            } catch (Exception e) {
                log.error("Node Filter exception", e);
                log.error("Node analysis exception", e.getMessage());
                throw new AppException(ErrorCodeEnum.NODE_ERROR);
            }
        });
    }

}

