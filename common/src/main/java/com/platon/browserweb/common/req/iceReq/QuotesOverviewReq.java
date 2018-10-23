package com.platon.browserweb.common.req.iceReq;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * User: dongqile
 * Date: 2018/9/6
 * Time: 17:15
 */
public class QuotesOverviewReq {
    /**
     * 代理商id
     */
    private long brokerId;

    /**
     * 计价货币
     */
    @NotEmpty
    private String quoteCurrency;

    public long getBrokerId () {
        return brokerId;
    }

    public void setBrokerId ( long brokerId ) {
        this.brokerId = brokerId;
    }

    public String getQuoteCurrency () {
        return quoteCurrency;
    }

    public void setQuoteCurrency ( String quoteCurrency ) {
        this.quoteCurrency = quoteCurrency;
    }
}