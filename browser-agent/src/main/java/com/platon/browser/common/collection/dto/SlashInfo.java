package com.platon.browser.common.collection.dto;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/4 13:53
 * @Description: 处罚信息Bean
 */
@Data
@Builder
@Accessors(chain = true)
public class SlashInfo {
    private BigInteger blockNumber;
    private BigInteger slashBlockCount;
    private BigInteger blockCount;
    private BigDecimal slashAmount;
    private Boolean kickOut;
    private Date slashTime;
}
