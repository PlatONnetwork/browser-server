package com.platon.browserweb.common.req.order;

import com.platon.browserweb.common.req.PageReq;

/**
 * User: dongqile
 * Date: 2018/7/4
 * Time: 21:30
 */
public class ListOpenOrderReq extends PageReq{

    /**
     * 委托报价方式
     MARKET：市价单
     LIMIT：现价单
     AMOUNT_MARKET：市价金额单
     AMOUNT_LIMIT：现价金额单
     */
    private String orderType;


    /**
     * 币对
     */
    private String symbol;


    /**
     * 委托方向
     1：Buy
     2：Sell
     */
    private String side;


    /**
     * 委托有效期规则
     1：Good Till Cancel (GTC)
     3：Immediate or Cancel (IOC)
     6：Good Till Date (GTD)
     7：Good Till Range (GTR)
     */
    private String timeInForce;


    /**
     * 代理商名称
     */
    private String brokerName;


    /**
     * criteria
     */
    private String criteria;



    /**
     * 查询时间
     */
    private Integer dateType = 30;

    /**
     * 计价货币代码
     */
    private String quoteCurrency;

    /**
     * 始（用户创建时间）
     */
    private long startTime;

    /**
     * 终（用户创建时间）
     */
    private long endTime;

    private long brokerId;

    private long exchangeId;

    private String type;

    private String isNoHis;

    private String ordStatus;

    private long userId;

    /**
     * 始（用户创建时间）
     */
    private String startTime2;

    /**
     * 终（用户创建时间）
     */
    private String endTime2;

    public String getQuoteCurrency () {
        return quoteCurrency;
    }

    public void setQuoteCurrency ( String quoteCurrency ) {
        this.quoteCurrency = quoteCurrency;
    }

    public long getUserId () {
        return userId;
    }

    public void setUserId ( long userId ) {
        this.userId = userId;
    }

    public String getOrderType () {
        return orderType;
    }

    public void setOrderType ( String orderType ) {
        this.orderType = orderType;
    }

    public String getSymbol () {
        return symbol;
    }

    public String getType () {
        return type;
    }

    public void setType ( String type ) {
        this.type = type;
    }

    public void setSymbol ( String symbol ) {
        this.symbol = symbol;
    }

    public String getSide () {
        return side;
    }

    public void setSide ( String side ) {
        this.side = side;
    }

    public String getTimeInForce () {
        return timeInForce;
    }

    public void setTimeInForce ( String timeInForce ) {
        this.timeInForce = timeInForce;
    }

    public String getBrokerName () {
        return brokerName;
    }

    public void setBrokerName ( String brokerName ) {
        this.brokerName = brokerName;
    }

    public String getCriteria () {
        return criteria;
    }

    public void setCriteria ( String criteria ) {
        this.criteria = criteria;
    }

    public Integer getDateType () {
        return dateType;
    }

    public void setDateType ( Integer dateType ) {
        this.dateType = dateType;
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

    public String getIsNoHis () {
        return isNoHis;
    }

    public void setIsNoHis ( String isNoHis ) {
        this.isNoHis = isNoHis;
    }

    public String getOrdStatus () {
        return ordStatus;
    }

    public void setOrdStatus ( String ordStatus ) {
        this.ordStatus = ordStatus;
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