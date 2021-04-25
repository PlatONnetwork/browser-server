package com.platon.browser.adjust;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class AdjustSummary {
    private String type="";
    private BigDecimal rewardSum=BigDecimal.ZERO;
    private BigDecimal lockSum=BigDecimal.ZERO;
    private BigDecimal hesSum=BigDecimal.ZERO;
}
