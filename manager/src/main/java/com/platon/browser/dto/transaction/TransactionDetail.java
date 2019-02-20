package com.platon.browser.dto.transaction;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.platon.browser.dao.entity.TransactionWithBLOBs;
import com.platon.browser.util.EnergonUtil;
import com.platon.browser.util.TxInfoResolver;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

@Data
public class TransactionDetail {
    private String txHash;
    private long timestamp;
    private int txReceiptStatus;
    private Long blockHeight;
    private String from;
    private String to;
    private String txType;
    private String value;
    private String actualTxCost;
    private String energonLimit;
    private String energonUsed;
    private String energonPrice;
    private String priceInE;
    private String priceInEnergon;
    private String inputData;
    private long expectTime;
    private String failReason;
    private long confirmNum;
    private String receiveType;
    private String txInfo;
    private String nodeName;
    private String nodeId;
    private BigDecimal deposit;
    private Integer voteCount;
    private BigDecimal ticketPrice;
    // 是否第一条
    private boolean first;
    // 是否最后一条
    private boolean last;
    @JsonIgnore
    private Long sequence;

    public void init(TransactionWithBLOBs initData) {
        BeanUtils.copyProperties(initData,this);
        this.setTxHash(initData.getHash());
        this.setBlockHeight(initData.getBlockNumber());
        this.setTimestamp(initData.getTimestamp().getTime());
        this.setInputData(initData.getInput());
        BigDecimal v=Convert.fromWei(initData.getActualTxCost(), Convert.Unit.ETHER);
        this.setActualTxCost(EnergonUtil.format(v));
        v = Convert.fromWei(initData.getValue(), Convert.Unit.ETHER);
        this.setValue(EnergonUtil.format(v));
        this.setPriceInE(initData.getEnergonPrice());
        v = Convert.fromWei(initData.getEnergonPrice(), Convert.Unit.ETHER);
        this.setPriceInEnergon(EnergonUtil.format(v));

        TxInfoResolver.resolve(txType,txInfo,value,this);
    }
}
