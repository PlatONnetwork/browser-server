package com.platon.browserweb.common.req.broker;

import com.platon.browserweb.common.req.PageReq;

/**
 * User: dongqile
 * Date: 2018/7/4
 * Time: 11:15
 */
public class UserAccountReq extends PageReq {


    /**
     * 代理商名称
     */
    private String brokerName;

    /**
     * 币种
     */
    private String currency;

    /**
     * 类型
     * COMPANY：企业
     * PERSONAL：个人
     */
    private String category;

    private long userId;

    private String criteria;

    private String startTime;

    private String endTime;

    private long brokerId;

    private long exchangeId;

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

    public String getStartTime () {
        return startTime;
    }

    public void setStartTime ( String startTime ) {
        this.startTime = startTime;
    }

    public String getEndTime () {
        return endTime;
    }

    public void setEndTime ( String endTime ) {
        this.endTime = endTime;
    }

    public String getCriteria () {
        return criteria;
    }

    public void setCriteria ( String criteria ) {
        this.criteria = criteria;
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

    public String getCategory () {
        return category;
    }

    public void setCategory ( String category ) {
        this.category = category;
    }

    public long getUserId () {
        return userId;
    }

    public void setUserId ( long userId ) {
        this.userId = userId;
    }
}