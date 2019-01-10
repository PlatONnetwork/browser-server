package com.platon.browser.dto.transaction;

import com.platon.browser.dao.entity.Transaction;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class TransactionPushItem {
    private String txHash;
    private String from;
    private String to;
    private String value;
    private Long blockHeight;
    private Integer transactionIndex;
    private Long timestamp;
    private String txType;
    private String receiveType;

    public void init(Transaction initData){
        BeanUtils.copyProperties(initData,this);
        this.setTxHash(initData.getHash());
        this.setBlockHeight(initData.getBlockNumber());
    }
}
