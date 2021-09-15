package com.platon.browser.bean;

import lombok.Data;

import java.math.BigInteger;

@Data
public class NodeSettleStatisBase {

    /**
     * 结算周期轮数
     */
    private BigInteger settleEpochRound;

    /**
     * 结算周期轮数对应的累计出块
     */
    private BigInteger blockNumGrandTotal;

    /**
     * 当选出块节点的次数
     */
    private BigInteger blockNumElected;

}
