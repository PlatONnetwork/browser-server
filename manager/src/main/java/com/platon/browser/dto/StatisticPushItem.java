package com.platon.browser.dto;

import com.platon.browser.dao.entity.Block;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.beans.BeanUtils;

@Data
@EqualsAndHashCode(callSuper = false)
public class StatisticPushItem {
    private Long height;
    private Long time;
    private Long transaction;

    public void init(Block initData){
        BeanUtils.copyProperties(initData,this);
        this.setHeight(initData.getNumber());
        this.setTime(initData.getTimestamp().getTime());
        this.setTransaction(initData.getTransactionNumber().longValue());
    }
}
