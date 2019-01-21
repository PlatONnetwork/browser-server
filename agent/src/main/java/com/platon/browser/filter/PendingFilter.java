package com.platon.browser.filter;

import com.platon.browser.common.dto.AnalysisResult;
import com.platon.browser.common.util.TransactionAnalysis;
import com.platon.browser.config.ChainsConfig;
import com.platon.browser.dao.entity.Block;
import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dao.mapper.PendingTxMapper;
import com.platon.browser.job.DataCollectorJob;
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
    public boolean analysis (DataCollectorJob.AnalysisParam param, Block block)throws  Exception {

        EthPendingTransactions ethPendingTransactions = chainsConfig.getWeb3j(chainId).ethPendingTx().send();
        //TODO:任务开始时间
        Date beginTime = new Date();
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
                //pendingTx.setNonce(transaction.getNonce().toString());
                pendingTx.setTimestamp(new Date() );
                pendingTx.setValue(valueConversion(transaction.getValue()));
                pendingTx.setInput(transaction.getInput());
                AnalysisResult analysisResult = TransactionAnalysis.analysis(!transaction.getInput().equals(null) ? transaction.getInput() : "0x",true);
                String type =  TransactionAnalysis.getTypeName(analysisResult.getType());
                pendingTx.setTxType(type);
                pendingTxes.add(pendingTx);
            }
        }

        Date endTime = new Date();
        String time = String.valueOf(endTime.getTime() - beginTime.getTime());
        //log.info("-------------------pendingTxAnalysis 执行时间："+time);

        return true;

    }

    public String valueConversion(BigInteger value){
        BigDecimal valueDiec = new BigDecimal(value.toString());
        BigDecimal conversionCoin = valueDiec.divide(new BigDecimal("1000000000000000000"));
        return  conversionCoin.toString();
    }

}