package com.platon.browser.engine.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

/**
 * @Auther: Chendongming
 * @Date: 2019/9/4 13:53
 * @Description: 处罚信息Bean
 */
@Data
public class SlashInfo {
    private BigInteger blockNumber;
    private BigInteger slashBlockCount;
    private BigInteger blockCount;
    private BigDecimal slashAmount;
    private boolean kickOut;
    private Date slashTime;
}
