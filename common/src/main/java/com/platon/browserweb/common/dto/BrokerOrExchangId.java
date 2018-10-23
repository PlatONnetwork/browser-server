package com.platon.browserweb.common.dto;

/**
 * User: dongqile
 * Date: 2018/6/30
 * Time: 14:56
 */
public class BrokerOrExchangId {

    private long borkerId;

    private long exchangeId;

    public long getBrokerId () {
        return borkerId;
    }

    public void setBorkerId ( long borkerId ) {
        this.borkerId = borkerId;
    }

    public long getExchangeId () {
        return exchangeId;
    }

    public void setExchangeId ( long exchangeId ) {
        this.exchangeId = exchangeId;
    }
}