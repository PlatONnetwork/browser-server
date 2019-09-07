
package com.platon.browser.config.bean;

import lombok.Data;

@Data
public class Common {
    private int ExpectedMinutes;
    private int NodeBlockTimeWindow;
    private int PerRoundBlocks;
    private int ValidatorCount;
    private int AdditionalCycleTime;
}
