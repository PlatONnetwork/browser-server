package com.platon.browser.complement.dao.param.delegate;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.platon.browser.common.enums.BusinessType;
import com.platon.browser.complement.dao.param.BusinessParam;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
public class DelegateExit implements BusinessParam {

    /**
     * 节点id
     */
    private String nodeId;

    /**
     * 委托金额
     */
    private BigDecimal amount;

    /**
     * 节点质押快高
     */
    private BigInteger stakingBlockNumber;

    /**
     * 最新委托阈值
     */
    private BigDecimal minimumThreshold;

    /**
     *  交易所在块高
     */
    private BigInteger blockNumber;

    /**
     * 交易发送方
     */
    private String txFrom;
    
    
    /**
     * 当前犹豫金额
     */
    private BigDecimal codeDelegateHes;

    /**
     * 扣减犹豫金额
     */
    private BigDecimal codeRmDelegateHes;

    /**
     * 当前锁定金额
     */
    private BigDecimal codeDelegateLocked;

    /**
     * 扣减锁定金额
     */
    private BigDecimal codeRmDelegateLocked;

    /**
     * 当前待赎回金额
     */
    private BigDecimal codeDelegateReleased;
    
    /**
     * 扣减待赎回金额
     */
    private BigDecimal codeRmDelegateReleased;

    /**
     * 当前是否为历史
     */
    private int codeIsHistory;

    /**
     * 真正退款金额
     */
    private BigDecimal codeRealAmount;

    /**
     * 节点是否退出
     */
    private boolean codeNodeIsLeave;

    @Override
    public BusinessType getBusinessType() {
        return BusinessType.DELEGATE_EXIT;
    }
}
