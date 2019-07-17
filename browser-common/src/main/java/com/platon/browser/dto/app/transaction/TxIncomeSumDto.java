package com.platon.browser.dto.app.transaction;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Auther: Chendongming
 * @Date: 2019/5/24 16:26
 * @Description:
 */
@Data
public class TxIncomeSumDto {
    private String txHash;
    private BigDecimal income;
}
