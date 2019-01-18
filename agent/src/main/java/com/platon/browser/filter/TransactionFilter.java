package com.platon.browser.filter;

import com.alibaba.fastjson.JSON;
import com.platon.browser.common.dto.AnalysisResult;
import com.platon.browser.common.util.TransactionAnalysis;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.TransactionExample;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dao.mapper.TransactionMapper;
import com.platon.browser.job.ChainInfoFilterJob;
import com.platon.browser.service.RedisCacheService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.*;

/**
 * User: dongqile
 * Date: 2019/1/7
 * Time: 14:28
 */
@Component
public class TransactionFilter {

    private static Logger log = LoggerFactory.getLogger(TransactionFilter.class);

    @Value("${chain.id}")
    private String chainId;

    @Value("${platon.redis.key.transaction}")
    private String transactionCacheKeyTemplate;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private ChainsConfig chainsConfig;

    //@Transactional
    public boolean transactionAnalysis(Map<String,Object> transactionReceiptMap, List <Transaction> transactionsList , long time)throws Exception{
        Date beginTime = new Date();
        log.debug("[into NodeFilter !!!...]");
        log.debug("[blockChain chainId is ]: " + chainId);
        boolean res = build(transactionReceiptMap,transactionsList, time);
        Date endTime = new Date();
        String time2 = String.valueOf(endTime.getTime()-beginTime.getTime());
        log.info("--------------------------------------transactionAnalysis :" + time2);
        return res;
    }


    public boolean build(Map<String,Object> transactionReceiptMap, List <Transaction> transactionsList , long time)throws Exception{
        Web3j web3j = chainsConfig.getWeb3j(chainId);
        //build database struct<Transaction>
        List<TransactionWithBLOBs> transactionWithBLOBsList = new ArrayList <>();
        Set<com.platon.browser.dao.entity.Transaction> transactionSet = new HashSet <>();
        //for loop transaction & transactionReceipt build database struct on PlatON
        List<String> txHashes = new ArrayList <>();
        for(Transaction transaction : transactionsList){
            if(null != transactionReceiptMap.get(transaction.getHash())){
                TransactionReceipt transactionReceipt = (TransactionReceipt) transactionReceiptMap.get(transaction.getHash());
                    txHashes.add(transaction.getHash());
                    com.platon.browser.dao.entity.Transaction transactions= new com.platon.browser.dao.entity.Transaction();
                    TransactionWithBLOBs transactionWithBLOBs = new TransactionWithBLOBs();
                    transactionWithBLOBs.setHash(transaction.getHash());
                    transactionWithBLOBs.setFrom(transaction.getFrom());
                    if (null != transaction.getTo()) {
                        //judge `to` address is accountAddress or contractAddress
                        EthGetCode ethGetCode = web3j.ethGetCode(transaction.getTo(), DefaultBlockParameterName.LATEST).send();
                        if ("0x".equals(ethGetCode.getCode())) {
                            transactionWithBLOBs.setTo("0x0000000000000000000000000000000000000000");
                            transactionWithBLOBs.setReceiveType("account");
                        } else {
                            transactionWithBLOBs.setTo(transaction.getTo());
                            transactionWithBLOBs.setReceiveType("contract");
                        }
                    } else {
                        transactionWithBLOBs.setTo("0x0000000000000000000000000000000000000000");
                        transactionWithBLOBs.setReceiveType("contract");
                    }
                    transactionWithBLOBs.setValue(transaction.getValue().toString());
                    transactionWithBLOBs.setTransactionIndex(transactionReceipt.getTransactionIndex().intValue());
                    transactionWithBLOBs.setEnergonPrice(transaction.getGasPrice().toString());
                    transactionWithBLOBs.setEnergonLimit(transaction.getGas().toString());
                    transactionWithBLOBs.setEnergonUsed(transactionReceipt.getGasUsed().toString());
                    transactionWithBLOBs.setNonce(transaction.getNonce().toString());
                    if (String.valueOf(time).length() == 10) {
                        transactionWithBLOBs.setTimestamp(new Date(time * 1000L));
                    } else {
                        transactionWithBLOBs.setTimestamp(new Date(time));
                    }
                    transactionWithBLOBs.setCreateTime(new Date());
                    transactionWithBLOBs.setUpdateTime(new Date());
                    if(null == transactionReceipt.getBlockNumber() ){
                        transactionWithBLOBs.setTxReceiptStatus(0);
                    }
                    transactionWithBLOBs.setTxReceiptStatus(1);
                    transactionWithBLOBs.setActualTxCost(transactionReceipt.getGasUsed().multiply(transaction.getGasPrice()).toString());
                    transactionWithBLOBs.setChainId(chainId);
                    transactionWithBLOBs.setBlockHash(transaction.getBlockHash());
                    transactionWithBLOBs.setBlockNumber(transaction.getBlockNumber().longValue());
                    if(transaction.getInput().length() <= 65535){
                        transactionWithBLOBs.setInput(transaction.getInput());
                    }else {
                        transactionWithBLOBs.setInput(transaction.getInput().substring(0,65535));
                    }
                    if(transaction.getInput().equals("0x") && transaction.getValue() != null){
                        transactionWithBLOBs.setTxType("transfer");
                    }
                    AnalysisResult analysisResult = TransactionAnalysis.analysis(transaction.getInput(),false);
                    if("1".equals(analysisResult.getType())){
                        analysisResult.setFunctionName("contract deploy");
                    }
                    String type =  TransactionAnalysis.getTypeName(analysisResult.getType());
                    transactionWithBLOBs.setTxType(type == null ? "transfer" : type);
                    String txinfo = JSON.toJSONString(analysisResult);
                    transactionWithBLOBs.setTxInfo(txinfo);
                    transactionWithBLOBsList.add(transactionWithBLOBs);
                    BeanUtils.copyProperties(transactionWithBLOBs,transactions);
                    transactionSet.add(transactions);
                }
            }
        //insert list into database
        //insert list into redis
        if(txHashes.size()>0){
            transactionMapper.batchInsert(transactionWithBLOBsList);
            TransactionExample condition = new TransactionExample();
            condition.createCriteria().andChainIdEqualTo(chainId).andHashIn(txHashes);
            List<com.platon.browser.dao.entity.Transaction> transactionList = transactionMapper.selectByExample(condition);
            redisCacheService.updateTransactionCache(chainId,new HashSet <>(transactionList));
        }
        return true;
    }

}