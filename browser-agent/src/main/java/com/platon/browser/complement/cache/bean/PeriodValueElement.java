package com.platon.browser.complement.cache.bean;

import lombok.Data;

import java.math.BigInteger;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/21 14:05
 * @Description: 利润和成本bean
 */
@Data
public class PeriodValueElement {
    public PeriodValueElement(){}
    public PeriodValueElement(BigInteger period, BigInteger value) {
        super();
        this.period = period;
        this.value = value;
    }

    private BigInteger period;
    private BigInteger value;
}
