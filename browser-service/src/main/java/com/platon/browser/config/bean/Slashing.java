
package com.platon.browser.config.bean;

import lombok.Data;

@Data
public class Slashing {
    private int PackAmountAbnormal;
    private int PackAmountHighAbnormal;
    private int PackAmountLowSlashRate;
    private int PackAmountHighSlashRate;
    private int DuplicateSignHighSlashing;
}
