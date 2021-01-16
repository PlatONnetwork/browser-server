package com.platon.browser.v0151.bean;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Erc20合约标识
 */
@Data
public class Erc721ContractId {
    private String name;
    private String symbol;
    private Integer decimal;
    private BigDecimal totalSupply;
}
