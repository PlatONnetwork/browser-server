package com.platon.browserweb.common.req.broker;

import com.platon.browserweb.common.req.currency.CurrenyReq;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * User: dongqile
 * Date: 2018/7/15
 * Time: 18:11
 */
public class BrokerSymbolsReq {

    /**
     * 代理商id
     */
    @NotNull
    private long userId;

    /**
     * 支持的币对
     */
    @NotEmpty
    private List <CurrenyReq> symbols;

    private String juwebName;

    private Integer juwebId;

    private long exchangeId;

    public long getExchangeId () {
        return exchangeId;
    }

    public void setExchangeId ( long exchangeId ) {
        this.exchangeId = exchangeId;
    }

    public String getJuwebName () {
        return juwebName;
    }

    public void setJuwebName ( String juwebName ) {
        this.juwebName = juwebName;
    }

    public Integer getJuwebId () {
        return juwebId;
    }

    public void setJuwebId ( Integer juwebId ) {
        this.juwebId = juwebId;
    }

    public long getUserId () {
        return userId;
    }

    public void setUserId ( long userId ) {
        this.userId = userId;
    }

    public List <CurrenyReq> getSymbols () {
        return symbols;
    }

    public void setSymbols ( List <CurrenyReq> symbols ) {
        this.symbols = symbols;
    }
}