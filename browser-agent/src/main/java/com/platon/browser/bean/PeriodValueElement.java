package com.platon.browser.bean;

import java.math.BigDecimal;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/21 14:05
 * @Description: 利润和成本bean
 */
public class PeriodValueElement {

    /**
     * 结算周期
     */
    private Long period;

    /**
     * 利润或成本
     */
    private BigDecimal value;

    public Long getPeriod() {
        return period;
    }

    public PeriodValueElement setPeriod(Long period) {
        this.period = period;
        return this;
    }

    public BigDecimal getValue() {
        return value;
    }

    public PeriodValueElement setValue(BigDecimal value) {
        this.value = value;
        return this;
    }

}
