package com.platon.browser.client;

import lombok.Data;

import java.math.BigInteger;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/15 19:27
 * @Description:
 */
@Data
public class RestrictingBalance {
    private String account;
    private BigInteger freeBalance;
    private BigInteger lockBalance;
}
