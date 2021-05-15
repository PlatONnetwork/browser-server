package com.platon.browser.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CountBalance {

    private int type;

    private BigDecimal free;

    private BigDecimal locked;

    public CountBalance() {
        this.free = new BigDecimal("0");
        this.locked = new BigDecimal("0");
    }

    public CountBalance(int type, BigDecimal free, BigDecimal locked) {
        this.type = type;
        this.free = free;
        this.locked = locked;
    }

}
