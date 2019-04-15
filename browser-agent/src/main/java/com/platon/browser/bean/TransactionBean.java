package com.platon.browser.bean;

import com.alibaba.fastjson.JSON;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.dto.AnalysisResult;
import com.platon.browser.util.TransactionAnalysis;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.Date;

public class TransactionBean extends TransactionWithBLOBs {
    public void init(Transaction transaction, TransactionReceipt receipt){
        // 使用原生交易信息填充交易实体
        this.setHash(transaction.getHash());
        this.setFrom(transaction.getFrom());
        this.setValue(transaction.getValue().toString());
        this.setEnergonPrice(transaction.getGasPrice().toString());
        this.setEnergonLimit(transaction.getGas().toString());
        this.setNonce(transaction.getNonce().toString());
        this.setCreateTime(new Date());
        this.setUpdateTime(new Date());
        this.setTxReceiptStatus(1);
        this.setBlockHash(transaction.getBlockHash());
        this.setBlockNumber(transaction.getBlockNumber().longValue());
        if(transaction.getInput().length() <= 65535){
            this.setInput(transaction.getInput());
        }else {
            this.setInput(transaction.getInput().substring(0,65535));
        }
        if(transaction.getInput().equals("0x") && transaction.getValue() != null){
            this.setTxType("transfer");
        }
        AnalysisResult analysisResult = TransactionAnalysis.analysis(transaction.getInput(),false);
        if("1".equals(analysisResult.getType())){
            analysisResult.setFunctionName("contract deploy");
            this.setTo(receipt.getContractAddress());
            this.setReceiveType("contract");
        }else this.setTo(transaction.getTo());
        String type =  TransactionAnalysis.getTypeName(analysisResult.getType());
        this.setTxType(type == null ? "transfer" : type);
        String txinfo = JSON.toJSONString(analysisResult);
        this.setTxInfo(txinfo);

        // 使用交易接收者信息填充交易实体
        this.setTransactionIndex(receipt.getTransactionIndex().intValue());
        this.setEnergonUsed(receipt.getGasUsed().toString());
        this.setActualTxCost(receipt.getGasUsed().multiply(transaction.getGasPrice()).toString());
        if(null == receipt.getBlockNumber() ){
            this.setTxReceiptStatus(0);
        }
    }
}
