package com.platon.browser.param.sync;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * totalSupply更新参数
 */
@Data
public class TotalSupplyUpdateParam {

    /**
     * 合约地址
     */
    private String address;

    /**
     * 供应总量
     */
    private BigDecimal totalSupply;

    /**
     * 更新时间
     */
    private Date updateTime;

}
