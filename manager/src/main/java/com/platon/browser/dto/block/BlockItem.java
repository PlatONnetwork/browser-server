package com.platon.browser.dto.block;

import com.platon.browser.dao.entity.Block;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class BlockItem {
    private long height;
    private long timestamp;
    private long transaction;
    private Integer size;
    private String miner;
    private String energonUsed;
    private String energonLimit;
    private String energonAverage;
    private String blockReward;
    private long serverTime;

    public void init(Block initData){
        BeanUtils.copyProperties(initData,this);
        this.setHeight(initData.getNumber());
        this.setTimestamp(initData.getTimestamp().getTime());
        this.setTransaction(initData.getTransactionNumber());
    }
}
