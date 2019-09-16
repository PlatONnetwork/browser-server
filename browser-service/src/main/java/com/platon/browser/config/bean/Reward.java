package com.platon.browser.config.bean;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class Reward {
    // 激励池分配给出块激励的比例 50%
    private BigDecimal NewBlockRate;
    // 一个参数，单位是年， 这个参数时间到达后，按NewBlockRate分配奖励给矿工
    private BigInteger PlatONFoundationYear;
}
