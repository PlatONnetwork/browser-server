package com.platon.browser.dto.transaction;

import com.platon.browser.dao.entity.PendingTx;
import com.platon.browser.util.EnergonUtil;
import com.platon.browser.util.TxInfoResolver;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.web3j.utils.Convert;

import java.math.BigDecimal;

@Data
public class PendingTxDetail {
    private String txHash;
    private long timestamp;
    private int txReceiptStatus;
    private String blockHeight;
    private String from;
    private String to;
    private String txType;
    private String value;
    private String actualTxCost;
    private int energonLimit;
    private int energonUsed;
    private String energonPrice;
    private String priceInE;
    private String priceInEnergon;
    private String inputData;
    private long expectTime;
    private String receiveType;
    private String txInfo;
    private String nodeName;
    private String nodeId;
    private BigDecimal deposit;
    private Integer voteCount;
    private BigDecimal ticketPrice;

    public void init(PendingTx initData) {
        BeanUtils.copyProperties(initData,this);
        this.setTxHash(initData.getHash());
        this.setTimestamp(initData.getTimestamp().getTime());
        BigDecimal v = Convert.fromWei(initData.getValue(), Convert.Unit.ETHER);
        this.setValue(EnergonUtil.format(v));
        this.setPriceInE(initData.getEnergonPrice());
        v = Convert.fromWei(initData.getEnergonPrice(), Convert.Unit.ETHER);
        this.setPriceInEnergon(EnergonUtil.format(v));

        TxInfoResolver.resolve(txType,txInfo,value,this);
    }
}
