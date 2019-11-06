package com.platon.browser.common.collection.dto;

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
@Accessors(chain = true)
public class PeriodValueElement {
    private Long period;
    private BigDecimal value;
}
