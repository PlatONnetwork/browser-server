package com.platon.browser.dto.transaction;

import com.platon.browser.dao.entity.PendingTx;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class PendingTxItem {
    private String txHash;
    private long timestamp;
    private String energonLimit;
    private String energonPrice;
    private String from;
    private String to;
    private String value;
    private String txType;
    private long serverTime;
    private String receiveType;

    public void init(PendingTx initData){
        BeanUtils.copyProperties(initData,this);
        this.setTxHash(initData.getHash());
        this.setTimestamp(initData.getTimestamp().getTime());
        this.setServerTime(System.currentTimeMillis());
    }
}
