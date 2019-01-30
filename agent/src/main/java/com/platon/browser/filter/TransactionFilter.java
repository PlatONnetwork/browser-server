package com.platon.browser.filter;

import com.platon.browser.bean.TransactionBean;
import com.platon.browser.client.PlatonClient;
import com.platon.browser.thread.AnalyseThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.io.IOException;
import java.util.*;

import static com.platon.browser.utils.CacheTool.RECEIVER_TO_TYPE;


/**
 * User: dongqile
 * Date: 2019/1/7
 * Time: 14:28
 */
@Component
public class TransactionFilter {
    private static Logger log = LoggerFactory.getLogger(TransactionFilter.class);
    @Value("${platon.redis.key.transaction}")
    private String transactionCacheKeyTemplate;
    @Autowired
    private PlatonClient platon;

    public List<TransactionBean> analyse(AnalyseThread.AnalyseParam param, long time) {
        Map<String,Object> transactionReceiptMap = param.transactionReceiptMap;
        List<TransactionBean> transactions = new ArrayList <>();
        param.transactions.forEach(initData -> {
            if(null != transactionReceiptMap.get(initData.getHash())){
                TransactionBean bean = new TransactionBean();
                TransactionReceipt receipt = (TransactionReceipt) transactionReceiptMap.get(initData.getHash());
                // Initialize the entity with the raw transaction and receipt
                bean.init(initData,receipt);
                // Convert timestamp into milliseconds
                if (String.valueOf(time).length() == 10) {
                    bean.setTimestamp(new Date(time * 1000L));
                } else {
                    bean.setTimestamp(new Date(time));
                }
                // Setup the chain id
                bean.setChainId(platon.getChainId());
                // Setup the receiver type
                if (null != initData.getTo()) {
                    bean.setTo(initData.getTo());
                    //judge `to` address is accountAddress or contractAddress
                    if(null != RECEIVER_TO_TYPE.get(initData.getTo())){
                        bean.setReceiveType(RECEIVER_TO_TYPE.get(initData.getTo()));
                    }else {
                        try {
                            EthGetCode ethGetCode = platon.getWeb3j().ethGetCode(initData.getTo(), DefaultBlockParameterName.LATEST).send();
                            if ("0x".equals(ethGetCode.getCode())) {
                                bean.setReceiveType("account");
                            } else {
                                bean.setReceiveType("contract");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                // Cache the receiver type for later use
                RECEIVER_TO_TYPE.put(initData.getTo(),bean.getReceiveType());
                transactions.add(bean);
            }
        });
        return transactions;
    }

}