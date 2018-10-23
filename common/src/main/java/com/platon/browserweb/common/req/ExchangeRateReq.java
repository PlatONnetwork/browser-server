package com.platon.browserweb.common.req;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotNull;

/**
 * User: dongqile
 * Date: 2018/6/25
 * Time: 18:09
 */
public class ExchangeRateReq {
    /**
     * 被兑换的货币
     */
    @NotNull
    private String fromCurrency;

    /**
     * 兑换成的货币
     */
    @NotNull
    private String toCurrency;

    public String getFromCurrency () {
        return fromCurrency;
    }

    public void setFromCurrency ( String fromCurrency ) {
        this.fromCurrency = fromCurrency;
    }

    public String getToCurrency () {
        return toCurrency;
    }

    public void setToCurrency ( String toCurrency ) {
        this.toCurrency = toCurrency;
    }
}