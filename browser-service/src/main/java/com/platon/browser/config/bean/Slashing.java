
package com.platon.browser.config.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Slashing {
    private BigDecimal PackAmountAbnormal;
    private BigDecimal PackAmountHighAbnormal;
    private BigDecimal PackAmountLowSlashRate;
    private BigDecimal PackAmountHighSlashRate;
    private BigDecimal DuplicateSignHighSlashing;
}
