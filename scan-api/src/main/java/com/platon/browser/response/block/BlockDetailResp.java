package com.platon.browser.response.block;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.json.CustomLatSerializer;

import java.math.BigDecimal;

/**
 * 区块详情返回对象
 *
 * @author zhangrj
 * @file BlockDetailResp.java
 * @description
 * @data 2019年8月31日
 */
public class BlockDetailResp {

    private Long number;

    private Long timestamp;

    private Integer txQty;

    private String hash;

    private String parentHash;

    private String nodeName;

    private String nodeId;

    private Long timeDiff;

    private String gasLimit;

    private String gasUsed;

    private String statTxGasLimit;

    private BigDecimal blockReward;

    private String extraData;

    /**
     * 是否第一条
     */
    private boolean first;

    /**
     * 是否最后一条
     */
    private boolean last;

    private Integer transferQty;

    private Integer delegateQty;

    private Integer stakingQty;

    private Integer proposalQty;

    private Long serverTime;

    private Integer size;

    /**
     * 附言
     */
    private String postscript;

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getParentHash() {
        return parentHash;
    }

    public void setParentHash(String parentHash) {
        this.parentHash = parentHash;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public Long getTimeDiff() {
        return timeDiff;
    }

    public void setTimeDiff(Long timeDiff) {
        this.timeDiff = timeDiff;
    }

    public String getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(String gasLimit) {
        this.gasLimit = gasLimit;
    }

    public String getGasUsed() {
        return gasUsed;
    }

    public void setGasUsed(String gasUsed) {
        this.gasUsed = gasUsed;
    }

    public String getStatTxGasLimit() {
        return statTxGasLimit;
    }

    public void setStatTxGasLimit(String statTxGasLimit) {
        this.statTxGasLimit = statTxGasLimit;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getBlockReward() {
        return blockReward;
    }

    public void setBlockReward(BigDecimal blockReward) {
        this.blockReward = blockReward;
    }

    public String getExtraData() {
        return extraData;
    }

    public void setExtraData(String extraData) {
        this.extraData = extraData;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public Long getServerTime() {
        return serverTime;
    }

    public void setServerTime(Long serverTime) {
        this.serverTime = serverTime;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getTxQty() {
        return txQty;
    }

    public void setTxQty(Integer txQty) {
        this.txQty = txQty;
    }

    public Integer getTransferQty() {
        return transferQty;
    }

    public void setTransferQty(Integer transferQty) {
        this.transferQty = transferQty;
    }

    public Integer getDelegateQty() {
        return delegateQty;
    }

    public void setDelegateQty(Integer delegateQty) {
        this.delegateQty = delegateQty;
    }

    public Integer getStakingQty() {
        return stakingQty;
    }

    public void setStakingQty(Integer stakingQty) {
        this.stakingQty = stakingQty;
    }

    public Integer getProposalQty() {
        return proposalQty;
    }

    public void setProposalQty(Integer proposalQty) {
        this.proposalQty = proposalQty;
    }

    public String getPostscript() {
        return postscript;
    }

    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }

}
