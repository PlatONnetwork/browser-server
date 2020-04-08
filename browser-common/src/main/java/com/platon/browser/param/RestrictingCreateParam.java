package com.platon.browser.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * User: dongqile
 * Date: 2019/8/6
 * Time: 16:10
 * txType=4000创建锁仓计划(创建锁仓)
 */
@Data
@Builder
@AllArgsConstructor
@Accessors(chain = true)
public class RestrictingCreateParam extends TxParam{

    /**
     * User: dongqile
     * Date: 2019/8/6
     * Time: 16:07
     * txType=4000创建锁仓计划(创建锁仓)plan
     */
    @Data
    @Builder
    @AllArgsConstructor
    @Accessors(chain = true)
    public static class RestrictingPlan {
        /**
         * 表示结算周期的倍数。与每个结算周期出块数的乘积表示在目标区块高度上释放锁定的资金。Epoch * 每周期的区块数至少要大于最高不可逆区块高度
         */
        private BigInteger epoch;

        /**
         * 表示目标区块上待释放的金额
         */
        private BigDecimal amount;
    }

    /**
     * 锁仓释放到账账户
     */
    private String account;

    /**
     * 锁仓具体计划
     */
    private List<RestrictingPlan> plans;
}
