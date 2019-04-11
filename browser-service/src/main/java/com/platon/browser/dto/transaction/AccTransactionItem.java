package com.platon.browser.dto.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.util.EnergonUtil;
import com.platon.browser.util.TxInfoResolver;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 账户详情中的交易列表Bean
 */
@Data
public class AccTransactionItem {

    private String txHash;
    private long blockTime;
    private String from;
    private String to;
    private String value;
    private String actualTxCost;
    private int txReceiptStatus;
    private String txType;
    private String txInfo;
    private long serverTime;
    private String failReason;
    private String receiveType;
    private BigDecimal ticketPrice;
    private BigDecimal income;
    private long voteCount;
    private long validVoteCount;
    private BigDecimal deposit;
    private String nodeId;
    private String nodeName;
    private int flag;

    @JsonIgnore
    private Date timestamp;

    public void init(Object initData){
        BeanUtils.copyProperties(initData,this);
        this.setServerTime(System.currentTimeMillis());

        String txHash = "",value = "0",cost = "0";
        if(initData instanceof TransactionWithBLOBs){
            TransactionWithBLOBs transaction = (TransactionWithBLOBs)initData;
            // 交易生成的时间就是出块时间
            this.setBlockTime(transaction.getTimestamp().getTime());
            txHash = transaction.getHash();
            value = transaction.getValue();
            cost = transaction.getActualTxCost();
        }

        if(initData instanceof PendingTx){
            PendingTx pendingTx = (PendingTx)initData;
            this.setTxReceiptStatus(-1); // 手动设置交易状态为pending
            txHash = pendingTx.getHash();
            value = pendingTx.getValue();
        }

        this.setTxHash(txHash);
        BigDecimal v = Convert.fromWei(value, Convert.Unit.ETHER);
        this.setValue(EnergonUtil.format(v));
        v = Convert.fromWei(cost, Convert.Unit.ETHER);
        this.setActualTxCost(EnergonUtil.format(v));

        if(deposit != null){
            v = Convert.fromWei(deposit, Convert.Unit.ETHER);
            this.setDeposit(new BigDecimal(EnergonUtil.format(v)));
        }else {
            this.setDeposit(new BigDecimal("0"));
        }
        TxInfoResolver.resolve(txType,txInfo,value,this);
        this.flag = 2;
    }
}
