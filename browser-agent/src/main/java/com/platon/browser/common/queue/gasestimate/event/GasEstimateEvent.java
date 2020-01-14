package com.platon.browser.common.queue.gasestimate.event;

import java.util.List;

/**
 * Gas price 估算事件
 */
public class GasEstimateEvent {
    // 消息唯一标识，防止重复处理：区块号*10000+交易index
    private Long seq;
    // 操作
    private ActionEnum action;
    private List<GasEstimateEpoch> epoches;

    public ActionEnum getAction() {
        return action;
    }

    public void setAction(ActionEnum action) {
        this.action = action;
    }

    public List<GasEstimateEpoch> getEpoches() {
        return epoches;
    }

    public void setEpoches(List<GasEstimateEpoch> epoches) {
        this.epoches = epoches;
    }
}
