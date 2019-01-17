package com.platon.browser.dto.block;

import com.platon.browser.dao.entity.Block;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
public class BlockListItem {
    private long height;
    private String hash;
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
        BigDecimal reward = Convert.fromWei(initData.getBlockReward(), Convert.Unit.ETHER).setScale(18, RoundingMode.DOWN);
        this.setBlockReward(reward.toString());
    }
}
