package com.platon.browser.dto;

import java.math.BigInteger;

import lombok.Data;

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

    private Integer decimal;

    private BigInteger totalSupply;
}
