package com.platon.browser.common.complement.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/21 14:05
 * @Description: 利润和成本bean
 */
@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class PeriodValueElement {
    private Long period;
    private BigDecimal value;
}
