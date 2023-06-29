package com.platon.browser.v0152.bean;

import com.platon.browser.enums.ErcTypeEnum;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 合约识别标识
 */
@Data
public class ErcContractId {

    private ErcTypeEnum typeEnum;

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
