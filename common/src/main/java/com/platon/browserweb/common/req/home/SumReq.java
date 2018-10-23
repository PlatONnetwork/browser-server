package com.platon.browserweb.common.req.home;

/**
 * User: dongqile
 * Date: 2018/7/12
 * Time: 13:56
 */
public class SumReq {

    private String type;

    private long brokerId;

    private long exchangeId;

    private long userId;

    public String getType () {
        return type;
    }

    public void setType ( String type ) {
        this.type = type;
    }

    public long getBrokerId () {
        return brokerId;
    }

    public void setBrokerId ( long brokerId ) {
        this.brokerId = brokerId;
    }

    public long getExchangeId () {
        return exchangeId;
    }

    public void setExchangeId ( long exchangeId ) {
        this.exchangeId = exchangeId;
    }

    public long getUserId () {
        return userId;
    }

    public void setUserId ( long userId ) {
        this.userId = userId;
    }
}

