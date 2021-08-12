package com.platon.browser.response.staking;

import java.math.BigDecimal;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.json.CustomLatSerializer;

/**
 * 验证人委托列表返回对象
 *
 * @author zhangrj
 * @file DelegationListByStakingResp.java
 * @description
 * @data 2019年8月31日
 */
public class DelegationListByStakingResp {

    private String delegateAddr; // 委托人地址

    private BigDecimal delegateValue; // 委托金额

    private BigDecimal delegateTotalValue;// 验证人委托的总金额

    private BigDecimal delegateLocked;    //已锁定委托（ATP）如果关联的验证人状态正常则正常显示，如果其他情况则为零（delegation）

    private BigDecimal delegateReleased; //当前验证人待赎回委托

    private Integer delegateAddrType;

    public String getDelegateAddr() {
        return delegateAddr;
    }

    public void setDelegateAddr(String delegateAddr) {
        this.delegateAddr = delegateAddr;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getDelegateValue() {
        return delegateValue;
    }

    public void setDelegateValue(BigDecimal delegateValue) {
        this.delegateValue = delegateValue;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getDelegateTotalValue() {
        return delegateTotalValue;
    }

    public void setDelegateTotalValue(BigDecimal delegateTotalValue) {
        this.delegateTotalValue = delegateTotalValue;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getDelegateLocked() {
        return delegateLocked;
    }

    public void setDelegateLocked(BigDecimal delegateLocked) {
        this.delegateLocked = delegateLocked;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getDelegateReleased() {
        return delegateReleased;
    }

    public void setDelegateReleased(BigDecimal delegateReleased) {
        this.delegateReleased = delegateReleased;
    }

    public Integer getDelegateAddrType() {
        return delegateAddrType;
    }

    public void setDelegateAddrType(Integer delegateAddrType) {
        this.delegateAddrType = delegateAddrType;
    }

}
