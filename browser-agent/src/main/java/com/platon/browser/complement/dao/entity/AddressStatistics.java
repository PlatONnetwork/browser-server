package com.platon.browser.complement.dao.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import lombok.Data;

@Data
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
}
