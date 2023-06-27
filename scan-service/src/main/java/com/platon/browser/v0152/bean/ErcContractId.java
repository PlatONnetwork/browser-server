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
    private String totalSupply;

    private Boolean isSupportErc20;
    private Boolean isSupportErc165;
    private Boolean isSupportErc721;
    private Boolean isSupportErc721Metadata;
    private Boolean isSupportErc721Enumeration;
    private Boolean isSupportErc1155;
    private Boolean isSupportErc1155Metadata;
}
