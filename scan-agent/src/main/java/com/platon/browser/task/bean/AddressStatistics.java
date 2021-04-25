package com.platon.browser.task.bean;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class AddressStatistics {
    private String stakingAddr;
    private BigDecimal stakingHes;
    private BigDecimal stakingLocked;
    private BigDecimal stakingReduction;
    private String delegateAddr;
    private BigDecimal delegateHes;
    private BigDecimal delegateLocked;
    private BigDecimal delegateReleased; 
    private String nodeId;
    private Set<String> nodeIdSet = new HashSet<>();

    public String getStakingAddr() {
        return stakingAddr;
    }

    public void setStakingAddr(String stakingAddr) {
        this.stakingAddr = stakingAddr;
    }

    public BigDecimal getStakingHes() {
        return stakingHes;
    }

    public void setStakingHes(BigDecimal stakingHes) {
        this.stakingHes = stakingHes;
    }

    public BigDecimal getStakingLocked() {
        return stakingLocked;
    }

    public void setStakingLocked(BigDecimal stakingLocked) {
        this.stakingLocked = stakingLocked;
    }

    public BigDecimal getStakingReduction() {
        return stakingReduction;
    }

    public void setStakingReduction(BigDecimal stakingReduction) {
        this.stakingReduction = stakingReduction;
    }

    public String getDelegateAddr() {
        return delegateAddr;
    }

    public void setDelegateAddr(String delegateAddr) {
        this.delegateAddr = delegateAddr;
    }

    public BigDecimal getDelegateHes() {
        return delegateHes;
    }

    public void setDelegateHes(BigDecimal delegateHes) {
        this.delegateHes = delegateHes;
    }

    public BigDecimal getDelegateLocked() {
        return delegateLocked;
    }

    public void setDelegateLocked(BigDecimal delegateLocked) {
        this.delegateLocked = delegateLocked;
    }

    public BigDecimal getDelegateReleased() {
        return delegateReleased;
    }

    public void setDelegateReleased(BigDecimal delegateReleased) {
        this.delegateReleased = delegateReleased;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public Set<String> getNodeIdSet() {
        return nodeIdSet;
    }

    public void setNodeIdSet(Set<String> nodeIdSet) {
        this.nodeIdSet = nodeIdSet;
    }
}
