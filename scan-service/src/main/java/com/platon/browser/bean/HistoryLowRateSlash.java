package com.platon.browser.bean;

import java.math.BigInteger;

/**
 * @description:
 * @author: chendongming@matrixelements.com
 * @create: 2019-11-22 10:18:43
 **/
public class HistoryLowRateSlash {
    // 被处罚节点Id
    //@JSONField(name = "NodeId")
    private String nodeId;
    // 处罚金额
    //@JSONField(name = "Amount")
    private BigInteger amount;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }
}
