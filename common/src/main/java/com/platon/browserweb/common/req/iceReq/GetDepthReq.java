package com.platon.browserweb.common.req.iceReq;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * User: dongqile
 * Date: 2018/9/6
 * Time: 16:45
 */
public class GetDepthReq {

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

    /**
     * 合并深度（不同币对数字不同，和tick_size相关），0-取整 1-1位小数  2-2位小数 3-3位小数
     */
    @NotNull
    private Integer merge;


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

    public Integer getMerge () {
        return merge;
    }

    public void setMerge ( Integer merge ) {
        this.merge = merge;
    }
}