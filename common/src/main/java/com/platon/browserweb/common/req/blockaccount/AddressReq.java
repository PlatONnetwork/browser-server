package com.platon.browserweb.common.req.blockaccount;

import com.platon.browserweb.common.req.PageReq;

/**
 * User: dongqile
 * Date: 2018/7/10
 * Time: 22:40
 */
public class AddressReq extends PageReq {
    /**
     * 代理商名称
     */
    private String brokerName;

    /**
     * 币种
     */
    private String currency;

    /**
     * 用户账户/用户姓名
     */
    private String criteria;

    /**
     * DEPOSIT：充币
     WITHDRAW：提币
     */
    private String bizCode;

    /**
     * 地址
     */
    private String address;

    private long userId;

    private long exchangeId;

    private String type;

    private long brokerId;

    public long getBrokerId () {
        return brokerId;
    }

    public void setBrokerId ( long brokerId ) {
        this.brokerId = brokerId;
    }

    public String getBrokerName () {
        return brokerName;
    }

    public void setBrokerName ( String brokerName ) {
        this.brokerName = brokerName;
    }

    public String getCurrency () {
        return currency;
    }

    public void setCurrency ( String currency ) {
        this.currency = currency;
    }

    public String getCriteria () {
        return criteria;
    }

    public void setCriteria ( String criteria ) {
        this.criteria = criteria;
    }

    public String getBizCode () {
        return bizCode;
    }

    public void setBizCode ( String bizCode ) {
        this.bizCode = bizCode;
    }

    public String getAddress () {
        return address;
    }

    public void setAddress ( String address ) {
        this.address = address;
    }

    public long getUserId () {
        return userId;
    }

    public void setUserId ( long userId ) {
        this.userId = userId;
    }

    public String getType () {
        return type;
    }

    public void setType ( String type ) {
        this.type = type;
    }

    public long getExchangeId () {
        return exchangeId;
    }

    public void setExchangeId ( long exchangeId ) {
        this.exchangeId = exchangeId;
    }
}