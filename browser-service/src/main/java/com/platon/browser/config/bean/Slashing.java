package com.platon.browser.config.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Slashing {
    // 违规-低出块率-触发处罚的出块率阈值 60%
    @JSONField(name = "PackAmountAbnormal")
    private BigDecimal packAmountAbnormal;
    // 区块双签扣除验证人自有质押金比例 100%
    @JSONField(name = "DuplicateSignHighSlashing")
    private BigDecimal duplicateSignHighSlashing;
    // 低出块率处罚多少个区块奖励
    @JSONField(name = "NumberOfBlockRewardForSlashing")
    private BigDecimal numberOfBlockRewardForSlashing;
}
