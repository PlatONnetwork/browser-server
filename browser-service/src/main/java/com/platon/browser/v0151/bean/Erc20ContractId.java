package com.platon.browser.v0151.bean;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * Erc20合约标识
 */
@Data
public class Erc20ContractId {
    private String name;
    private String symbol;
    private Integer decimal;
    private BigDecimal totalSupply;
    public boolean isSupportErc20(){
        return !StringUtils.isBlank(name) && !StringUtils.isBlank(symbol) && decimal != null && totalSupply != null;
    }
}
