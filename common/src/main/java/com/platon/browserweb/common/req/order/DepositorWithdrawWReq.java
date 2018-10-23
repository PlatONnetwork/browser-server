package com.platon.browserweb.common.req.order;

import com.platon.browserweb.common.req.PageReq;

/**
 * User: dongqile
 * Date: 2018/7/6
 * Time: 15:33
 */
public class DepositorWithdrawWReq extends PageReq {

    /**
     * 货币代码，ETH BTC
     */
    private String currency;

    /**
     * 代理商名称
     */
    private String brokerName;

    /**
     * 交易hash
     */
    private String hash;

    /**
     * 用户账户/用户姓名
     */
    private String criteria;

    /**
     * 始（用户创建时间）
     */
    private long startTime;

    /**
     * 终（用户创建时间）
     */
    private long endTime;

    /**
     * 查询时间
     */
    private Integer dateType =30;

    /**
     * 代理商Id
     */
    private long brokerId;

    /**
     * 交易所Id
     */
    private long exchangeId;

    /**
     * 提币状态
     1：处理中
     2：成功
     3：失败
     */
    private String queryStatus;


    private String type;

    /**
     * 始（用户创建时间）
     */
    private String startTime2;

    /**
     * 终（用户创建时间）
     */
    private String endTime2;

    private String criteria2;

    public String getCriteria2 () {
        return criteria2;
    }

    public void setCriteria2 ( String criteria2 ) {
        this.criteria2 = criteria2;
    }

    public String getQueryStatus () {
        return queryStatus;
    }

    public void setQueryStatus ( String queryStatus ) {
        this.queryStatus = queryStatus;
    }

    public String getCurrency () {
        return currency;
    }

    public void setCurrency ( String currency ) {
        this.currency = currency;
    }

    public String getBrokerName () {
        return brokerName;
    }

    public void setBrokerName ( String brokerName ) {
        this.brokerName = brokerName;
    }

    public String getHash () {
        return hash;
    }

    public void setHash ( String hash ) {
        this.hash = hash;
    }

    public String getCriteria () {
        return criteria;
    }

    public void setCriteria ( String criteria ) {
        this.criteria = criteria;
    }

    public long getStartTime () {
        return startTime;
    }

    public void setStartTime ( long startTime ) {
        this.startTime = startTime;
    }

    public long getEndTime () {
        return endTime;
    }

    public void setEndTime ( long endTime ) {
        this.endTime = endTime;
    }

    public Integer getDateType () {
        return dateType;
    }

    public void setDateType ( Integer dateType ) {
        this.dateType = dateType;
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

    public String getType () {
        return type;
    }

    public void setType ( String type ) {
        this.type = type;
    }

    public String getStartTime2 () {
        return startTime2;
    }

    public void setStartTime2 ( String startTime2 ) {
        this.startTime2 = startTime2;
    }

    public String getEndTime2 () {
        return endTime2;
    }

    public void setEndTime2 ( String endTime2 ) {
        this.endTime2 = endTime2;
    }


}