package com.platon.browser.filter;

import com.platon.browser.bean.TransactionBean;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.thread.AnalyseThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
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
    private ChainsConfig chainsConfig;

    public final static Map<String,String> toAddressTypeMap = new HashMap<>();

    public List<TransactionBean> analyse(AnalyseThread.AnalyseParam param, long time) {
        Map<String,Object> transactionReceiptMap = param.transactionReceiptMap;
        Web3j web3j = chainsConfig.getWeb3j(chainId);
        List<TransactionBean> transactions = new ArrayList <>();
        param.transactions.forEach(initData -> {
            if(null != transactionReceiptMap.get(initData.getHash())){
                TransactionReceipt receipt = (TransactionReceipt) transactionReceiptMap.get(initData.getHash());
                TransactionBean transaction = new TransactionBean();
                // Initialize the entity with the raw transaction and receipt
                transaction.init(initData,receipt);
                // Convert timestamp into milliseconds
                if (String.valueOf(time).length() == 10) {
                    transaction.setTimestamp(new Date(time * 1000L));
                } else {
                    transaction.setTimestamp(new Date(time));
                }
                // Setup the chain id
                transaction.setChainId(chainId);
                // Setup the receiver type
                if (null != initData.getTo()) {
                    transaction.setTo(initData.getTo());
                    //judge `to` address is accountAddress or contractAddress
                    if(null != toAddressTypeMap.get(initData.getTo())){
                        transaction.setReceiveType(toAddressTypeMap.get(initData.getTo()));
                    }else {
                        try {
                            EthGetCode ethGetCode = web3j.ethGetCode(initData.getTo(), DefaultBlockParameterName.LATEST).send();
                            if ("0x".equals(ethGetCode.getCode())) {
                                transaction.setReceiveType("account");
                            } else {
                                transaction.setReceiveType("contract");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // Cache the receiver type for later use
                toAddressTypeMap.put(initData.getTo(),transaction.getReceiveType());
                transactions.add(transaction);
            }
        });
        return transactions;
    }

}