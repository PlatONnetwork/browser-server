package com.platon.browser.common.complement.dto;

import java.math.BigDecimal;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/21 14:05
 * @Description: 利润和成本bean
 */
public class PeriodValueElement {
    private Long period;
    private BigDecimal value;

    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
