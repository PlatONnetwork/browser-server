package com.platon.browser.bean;

import com.platon.browser.dao.entity.GasEstimate;

import java.util.List;

/**
 * Gas price 估算事件
 */
public class GasEstimateEvent {
    // 消息唯一标识，防止重复处理：区块号*10000+交易index
    private Long seq;
    // 操作
    private List<GasEstimate> estimateList;

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public List<GasEstimate> getEstimateList() {
        return estimateList;
    }

    public void setEstimateList(List<GasEstimate> estimateList) {
        this.estimateList = estimateList;
    }
}
