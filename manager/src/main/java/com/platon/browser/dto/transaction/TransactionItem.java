package com.platon.browser.dto.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.platon.browser.dao.entity.Transaction;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

@Data
public class TransactionItem {
    private String txHash;
    private Long blockHeight;
    private Long blockTime;
    private String from;
    private String to;
    private String value;
    private String actualTxCost;
    private Integer txReceiptStatus;
    private String txType;
    private Long serverTime;
    private String failReason;
    // to字段存储的账户类型：account-钱包地址，contract-合约地址
    private String receiveType;

    public void init(Transaction initData){
        BeanUtils.copyProperties(initData,this);
        this.setTxHash(initData.getHash());
        this.setBlockHeight(initData.getBlockNumber());
        this.setServerTime(serverTime);
        // 交易时间就是出块时间
        this.setBlockTime(initData.getTimestamp().getTime());
        BigDecimal txCost = new BigDecimal(initData.getActualTxCost());
        txCost=Convert.fromWei(txCost, Convert.Unit.ETHER);
        this.setActualTxCost(String.valueOf(txCost));
    }

    @JsonIgnore
    private long timestamp;
}
