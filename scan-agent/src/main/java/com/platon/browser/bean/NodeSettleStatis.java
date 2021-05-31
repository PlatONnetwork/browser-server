package com.platon.browser.bean;

import lombok.Data;

import java.math.BigInteger;

/**
 * 节点结算周期的出块统计
 *
 * @date 2021/5/25
 */
@Data
public class NodeSettleStatis {

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 结算周期轮数
     */
    private BigInteger issueEpochRound;

    /**
     * 结算周期轮数对应的累计出块
     */
    private BigInteger blockNumGrandTotal;

    /**
     * 当选出块节点的次数
     */
    private BigInteger blockNumElected;

}
