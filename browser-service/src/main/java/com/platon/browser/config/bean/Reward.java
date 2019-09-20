package com.platon.browser.config.bean;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class Reward {
    // 激励池分配给出块激励的比例 50%
    @JSONField(name = "NewBlockRate")
    private BigDecimal newBlockRate;
    // 一个参数，单位是年， 这个参数时间到达后，按NewBlockRate分配奖励给矿工
    @JSONField(name = "PlatONFoundationYear")
    private BigInteger platONFoundationYear;
}
