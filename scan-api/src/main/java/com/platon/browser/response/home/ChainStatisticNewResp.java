package com.platon.browser.response.home;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.json.CustomLatSerializer;

/**
 * 首页统计返回对象
 *
 * @author zhangrj
 * @file ChainStatisticNewResp.java
 * @description
 * @data 2019年8月31日
 */
public class ChainStatisticNewResp {

    private Long currentNumber; // 当前区块高度

    private String nodeId; // 出块节点id

    private String nodeName; // 出块节点名称

    private Integer txQty; // 总的交易数

    private Integer currentTps; // 当前的TPS

    private Integer maxTps; // 最大交易TPS

    private BigDecimal turnValue; // 当前流通量

    private BigDecimal issueValue; // 当前发行量

    private BigDecimal stakingDelegationValue; // 当前质押总数=有效的质押+委托

    private Integer addressQty; // 地址数

    private Integer proposalQty; // 总提案数

    private Integer doingProposalQty; // 进行中提案数

    private Integer nodeNum; // 节点数

    private BigDecimal availableStaking;// 总可用质押

    private List<BlockListNewResp> blockList;

    /**
     * 质押的分母 = 总发行量 - 实时激励池余额 - 实时委托奖励池合约余额
     */
    private BigDecimal stakingDenominator;

    public Long getCurrentNumber() {
        return currentNumber;
    }

    public void setCurrentNumber(Long currentNumber) {
        this.currentNumber = currentNumber;
    }

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

    public Integer getTxQty() {
        return txQty;
    }

    public void setTxQty(Integer txQty) {
        this.txQty = txQty;
    }

    public Integer getCurrentTps() {
        return currentTps;
    }

    public void setCurrentTps(Integer currentTps) {
        this.currentTps = currentTps;
    }

    public Integer getMaxTps() {
        return maxTps;
    }

    public void setMaxTps(Integer maxTps) {
        this.maxTps = maxTps;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getTurnValue() {
        return turnValue;
    }

    public void setTurnValue(BigDecimal turnValue) {
        this.turnValue = turnValue;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getIssueValue() {
        return issueValue;
    }

    public void setIssueValue(BigDecimal issueValue) {
        this.issueValue = issueValue;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getStakingDelegationValue() {
        return stakingDelegationValue;
    }

    public void setStakingDelegationValue(BigDecimal stakingDelegationValue) {
        this.stakingDelegationValue = stakingDelegationValue;
    }

    public Integer getAddressQty() {
        return addressQty;
    }

    public void setAddressQty(Integer addressQty) {
        this.addressQty = addressQty;
    }

    public Integer getProposalQty() {
        return proposalQty;
    }

    public void setProposalQty(Integer proposalQty) {
        this.proposalQty = proposalQty;
    }

    public Integer getDoingProposalQty() {
        return doingProposalQty;
    }

    public void setDoingProposalQty(Integer doingProposalQty) {
        this.doingProposalQty = doingProposalQty;
    }

    public List<BlockListNewResp> getBlockList() {
        return blockList;
    }

    public void setBlockList(List<BlockListNewResp> blockList) {
        this.blockList = blockList;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getAvailableStaking() {
        return availableStaking;
    }

    public void setAvailableStaking(BigDecimal availableStaking) {
        this.availableStaking = availableStaking;
    }

    public Integer getNodeNum() {
        return nodeNum;
    }

    public void setNodeNum(Integer nodeNum) {
        this.nodeNum = nodeNum;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getStakingDenominator() {
        return stakingDenominator;
    }

    public void setStakingDenominator(BigDecimal stakingDenominator) {
        this.stakingDenominator = stakingDenominator;
    }

}
