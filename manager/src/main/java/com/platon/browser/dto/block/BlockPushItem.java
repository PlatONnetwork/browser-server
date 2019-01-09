package com.platon.browser.dto.block;

import com.platon.browser.dao.entity.Block;
import lombok.Data;
import org.springframework.beans.BeanUtils;

@Data
public class BlockPushItem {
    private Long height;
    private Long timestamp;
    private Long serverTime;
    private String node;
    private Integer transaction;
    private String blockReward;

    public void init(Block initData){
        BeanUtils.copyProperties(initData,this);
        this.setNode(initData.getMiner());
        this.setHeight(initData.getNumber());
        this.setTimestamp(initData.getTimestamp().getTime());
        this.setTransaction(initData.getTransactionNumber());

        this.setServerTime(System.currentTimeMillis());
    }
}
