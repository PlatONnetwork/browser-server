package com.platon.browser.bean;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 地址委托返回数据
 *
 * @author zhangrj
 * @file DelegationStaking.java
 * @description
 * @data 2019年11月12日
 */
@Data
public class DelegationStaking {

    private String delegateAddr;

    private BigDecimal delegateHes;

    private BigDecimal delegateLocked;

    private BigDecimal delegateReleased;

    private Integer delegateAddrType;

}
