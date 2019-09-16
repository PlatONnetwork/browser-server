package com.platon.browser.config.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Slashing {
    // 违规-低出块率-触发处罚的出块率阈值 60%
    private BigDecimal PackAmountAbnormal;
    // 区块双签扣除验证人自有质押金比例 100%
    private BigDecimal DuplicateSignHighSlashing;
    // 低出块率处罚多少个区块奖励
    private BigDecimal NumberOfBlockRewardForSlashing;
}
