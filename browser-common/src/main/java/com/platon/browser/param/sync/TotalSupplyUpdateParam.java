package com.platon.browser.param.sync;

import lombok.Data;

import java.math.BigDecimal;

/**
 * totalSupply更新参数
 */
@Data
public class TotalSupplyUpdateParam {
    private String address;
    private BigDecimal totalSupply;
}
