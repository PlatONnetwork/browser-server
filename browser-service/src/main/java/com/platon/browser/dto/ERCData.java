package com.platon.browser.dto;

import lombok.Data;

import java.math.BigInteger;

/**
 * @program: browser-server
 * @description: erc data
 * @author: Rongjin Zhang
 * @create: 2020-09-23 11:00
 */
@Data
public class ERCData {

    private String name;

    private String symbol;

    private BigInteger decimal;

    private BigInteger totalSupply;
}
