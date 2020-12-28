package com.platon.browser.service.erc20;

import java.math.BigInteger;

/**
 * @program: browser-server
 * @description: erc data
 * @author: Rongjin Zhang
 * @create: 2020-09-23 11:00
 */
public class ERCData {

    private String name;
    private String symbol;
    private Integer decimal;
    private BigInteger totalSupply;

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return this.symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Integer getDecimal() {
        return this.decimal;
    }

    public void setDecimal(Integer decimal) {
        this.decimal = decimal;
    }

    public BigInteger getTotalSupply() {
        return this.totalSupply;
    }

    public void setTotalSupply(BigInteger totalSupply) {
        this.totalSupply = totalSupply;
    }
}
