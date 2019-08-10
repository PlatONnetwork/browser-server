package com.platon.browser.dto.json;

import lombok.Data;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 16:07
 * txType=4000创建锁仓计划(创建锁仓)plan
 */
@Data
public class PlanDto {
    /**
     * 表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。Epoch * 每周期的区块数至少要大于最高不可逆区块高度
     */
    private Integer epoch;

    /**
     * 表示目标区块上待释放的金额
     */
    private String amount;
}