package com.platon.browser.bean;

import lombok.Builder;
import lombok.experimental.Accessors;

import java.math.BigInteger;

@Builder
@Accessors(chain = true)
public class NodeItem {

    private String nodeId;

    private String nodeName;

    // 最新的质押区块号,随验证人创建交易更新
    private BigInteger stakingBlockNum;

    /**
     * 节点结算周期的出块统计信息
     */
    private String nodeSettleStatisInfo;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public BigInteger getStakingBlockNum() {
        return stakingBlockNum;
    }

    public void setStakingBlockNum(BigInteger stakingBlockNum) {
        this.stakingBlockNum = stakingBlockNum;
    }

    public String getNodeSettleStatisInfo() {
        return nodeSettleStatisInfo;
    }

    public void setNodeSettleStatisInfo(String nodeSettleStatisInfo) {
        this.nodeSettleStatisInfo = nodeSettleStatisInfo;
    }

}
