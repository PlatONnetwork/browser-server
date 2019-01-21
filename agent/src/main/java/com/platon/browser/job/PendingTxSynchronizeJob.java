package com.platon.browser.job;

import com.platon.browser.common.dto.AnalysisResult;
import com.platon.browser.common.util.TransactionAnalysis;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.PendingTx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetCode;
import org.web3j.protocol.core.methods.response.EthPendingTransactions;
import org.web3j.protocol.core.methods.response.Transaction;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: dongqile
 * Date: 2018/10/25
 * Time: 18:07
 */
@Component
public class PendingTxSynchronizeJob {

    /**
     * 挂起交易同步任务
     * 1.根据web3j配置文件获取节点信息
     * 2.构建web3jclient
     * 3.同步链上挂起交易列表
     * 4.数据整合推送至rabbitMQ队列
     */

    private static Logger log = LoggerFactory.getLogger(PendingTxSynchronizeJob.class);

    @Value("${chain.id}")
    private String chainId;

    @Autowired
    private ChainsConfig chainsConfig;

    @Scheduled(cron = "0/1 * * * * ?")
    protected void doJob () {
        try {
            EthPendingTransactions ethPendingTransactions = chainsConfig.getWeb3j(chainId).ethPendingTx().send();
            Web3j web3j = chainsConfig.getWeb3j(chainId);
            List <Transaction> list = ethPendingTransactions.getTransactions();
            List <PendingTx> pendingTxes = new ArrayList <>();
            if (!list.equals(null) && list.size() > 0) {
                for (Transaction transaction : list) {
                    PendingTx pendingTx = new PendingTx();
                    pendingTx.setHash(transaction.getHash());
                    pendingTx.setFrom(transaction.getFrom());
                    pendingTx.setTo(transaction.getTo());
                    if (null != transaction.getTo()) {
                        EthGetCode ethGetCode = web3j.ethGetCode(transaction.getTo(), DefaultBlockParameterName.LATEST).send();
                        if ("0x".equals(ethGetCode.getCode())) {
                            pendingTx.setReceiveType("account");
                        } else {
                            pendingTx.setReceiveType("contract");
                        }
                    } else {
                        pendingTx.setTo("0x");
                    }
                    pendingTx.setEnergonLimit(transaction.getGas().toString());
                    pendingTx.setEnergonPrice(transaction.getGasPrice().toString());
                    pendingTx.setTimestamp(new Date());
                    pendingTx.setValue(valueConversion(transaction.getValue()));
                    pendingTx.setInput(transaction.getInput());
                    AnalysisResult analysisResult = TransactionAnalysis.analysis(!transaction.getInput().equals(null) ? transaction.getInput() : "0x", true);
                    String type = TransactionAnalysis.getTypeName(analysisResult.getType());
                    pendingTx.setTxType(type);
                    pendingTxes.add(pendingTx);
                    log.debug("PendingTx Synchronization is complete !!!...");
                }
            }
            log.debug("PendingTxSynchronizeJob is null ,Synchronization is complete !!!...");
        } catch (Exception e) {
            log.error("PendingTxSynchronizeJob Exception",e.getMessage());
        }

    }

    public String valueConversion ( BigInteger value ) {
        BigDecimal valueDiec = new BigDecimal(value.toString());
        BigDecimal conversionCoin = valueDiec.divide(new BigDecimal("1000000000000000000"));
        return conversionCoin.toString();
    }

}
