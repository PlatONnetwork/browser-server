package com.platon.browser.filter;

import com.platon.browser.common.dto.AnalysisResult;
import com.platon.browser.common.util.TransactionAnalysis;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dao.mapper.PendingTxMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
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
 * Date: 2019/1/9
 * Time: 17:08
 */
@Component
public class PendingFilter {

    private static Logger log = LoggerFactory.getLogger(PendingFilter.class);

    @Value("${chain.id}")
    private String chainId;

    @Autowired
    private PendingTxMapper pendingTxMapper;

    @Autowired
    private ChainsConfig chainsConfig;

    @Transactional
    public void analysis() {

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
                    if (null != transaction.getTo()) {
                        pendingTx.setTo(transaction.getTo());
                        EthGetCode ethGetCode = web3j.ethGetCode(transaction.getTo(), DefaultBlockParameterName.LATEST).send();
                        if ("0x".equals(ethGetCode.getCode())) {
                            pendingTx.setReceiveType("account");
                        } else {
                            pendingTx.setReceiveType("contract");
                        }
                    } else {
                        pendingTx.setTo("0x0000000000000000000000000000000000000000");
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
                pendingTxMapper.batchInsert(pendingTxes);
            }
            log.debug("PendingTxSynchronizeJob is null ,Synchronization is complete !!!...");
        } catch (Exception e) {
            log.error("PendingTxSynchronizeJob Exception",e.getMessage());
        }
    }

    public String valueConversion(BigInteger value){
        BigDecimal valueDiec = new BigDecimal(value.toString());
        BigDecimal conversionCoin = valueDiec.divide(new BigDecimal("1000000000000000000"));
        return  conversionCoin.toString();
    }

}