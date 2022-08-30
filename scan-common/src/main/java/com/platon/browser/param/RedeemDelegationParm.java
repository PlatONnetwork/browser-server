package com.platon.browser.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class RedeemDelegationParm extends TxParam {

    /**
     * 状态码
     */
    private String status;

    /**
     * 成功领取的委托金,回到余额
     */
    private BigDecimal released;

    /**
     * 成功领取的委托金,回到锁仓账户
     */
    private BigDecimal restrictingPlan;

}
