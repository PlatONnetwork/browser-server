package com.platon.browser.v0151.bean;

import com.platon.browser.v0151.enums.ErcTypeEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

/**
 * Erc20合约标识
 */
@Data
public class ErcContractId {
    private ErcTypeEnum typeEnum=ErcTypeEnum.UNKNOWN;
    private String name;
    private String symbol;
    private Integer decimal;
    private BigDecimal totalSupply;
}
