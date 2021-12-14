package com.platon.browser.v0152.bean;

import com.platon.browser.enums.ErcTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Erc20合约标识
 */
@Data
public class ErcContractId {

    private ErcTypeEnum typeEnum = ErcTypeEnum.UNKNOWN;

    /**
     * 合约名称
     */
    private String name;

    /**
     * 合约符号
     */
    private String symbol;

    /**
     * 合约精度
     */
    private Integer decimal;

    /**
     * 供应总量
     */
    private BigDecimal totalSupply;

}
