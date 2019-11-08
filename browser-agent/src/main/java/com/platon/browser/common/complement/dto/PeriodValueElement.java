package com.platon.browser.common.complement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

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
