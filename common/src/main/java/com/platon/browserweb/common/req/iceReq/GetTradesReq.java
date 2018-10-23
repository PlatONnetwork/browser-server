package com.platon.browserweb.common.req.iceReq;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * User: dongqile
 * Date: 2018/9/6
 * Time: 17:11
 */
public class GetTradesReq {

    /**
     * 代理商id
     */

    private long brokerId;

    /**
     * 货币对
     */
    @NotEmpty
    private String symbol;

    /**
     * 指定获取数据的条数
     */
    @NotNull
    private Integer size;

    public long getBrokerId () {
        return brokerId;
    }

    public void setBrokerId ( long brokerId ) {
        this.brokerId = brokerId;
    }

    public String getSymbol () {
        return symbol;
    }

    public void setSymbol ( String symbol ) {
        this.symbol = symbol;
    }

    public Integer getSize () {
        return size;
    }

    public void setSize ( Integer size ) {
        this.size = size;
    }
}