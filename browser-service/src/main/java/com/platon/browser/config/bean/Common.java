
package com.platon.browser.config.bean;

import lombok.Data;

import java.math.BigInteger;

@Data
public class Common {
    private BigInteger ExpectedMinutes;
    private BigInteger NodeBlockTimeWindow;
    private BigInteger PerRoundBlocks;
    private BigInteger ValidatorCount;
    private BigInteger AdditionalCycleTime;
}
