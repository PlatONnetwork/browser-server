package com.platon.browser.dto.transaction;

import com.platon.browser.dao.entity.PendingTx;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

    public void init(PendingTx initData) {
        BeanUtils.copyProperties(initData,this);
        this.setTxHash(initData.getHash());
        this.setTimestamp(initData.getTimestamp().getTime());
        BigDecimal v = Convert.fromWei(initData.getValue(), Convert.Unit.ETHER).setScale(18, RoundingMode.DOWN);
        this.setValue(String.valueOf(v.doubleValue()));
        this.setPriceInE(initData.getEnergonPrice());
        v = Convert.fromWei(initData.getEnergonPrice(), Convert.Unit.ETHER).setScale(18,RoundingMode.DOWN);
        this.setPriceInEnergon(String.valueOf(v.doubleValue()));
    }
}
