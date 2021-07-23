package com.platon.browser.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecoveredDelegationAmount {

    /**
     * 地址
     */
    private String delegateAddr;
    /**
     * 质押快高
     */
    private Long stakingBlockNum;
    /**
     * 节点id
     */
    private String nodeId;
    /**
     * 找回的委托奖励
     */
    private BigDecimal recoveredDelegationAmount;

}
