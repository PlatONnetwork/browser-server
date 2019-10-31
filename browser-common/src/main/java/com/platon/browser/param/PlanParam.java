package com.platon.browser.param;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 16:07
 * txType=4000创建锁仓计划(创建锁仓)plan
 */
@Data
public class PlanParam {
    /**
     * 表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。Epoch * 每周期的区块数至少要大于最高不可逆区块高度
     */
    private Integer epoch;

    /**
     * 表示目标区块上待释放的金额
     */
    private String amount;
    /********把字符串类数值转换为大浮点数的便捷方法********/
    public BigDecimal decimalAmount(){return StringUtils.isBlank(amount)?BigDecimal.ZERO:new BigDecimal(amount);}
    /********把字符串类数值转换为大整数的便捷方法********/
    public BigInteger integerAmount(){return StringUtils.isBlank(amount)?BigInteger.ZERO:new BigInteger(amount);}
}
