package com.platon.browser.bean;

import lombok.Data;

import java.math.BigInteger;

/**
 * @Author: NieXiang
 * @Date: 2023/6/7
 */
@Data
public class UnusualTransfer {

    private String txHash;

    private String from;

    private String to;

    private BigInteger amount;

}
