package com.platon.browserweb.common.req.order;

import com.platon.browserweb.common.req.PageReq;
import com.platon.browserweb.common.validate.OrderIdGroup;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * User: dongqile
 * Date: 2018/7/6
 * Time: 11:56
 */
public class ListTradeReq extends PageReq {

    /**
     * 币对
     */
    private String symbol;

    /**
     * 买单用户账号/卖单用户账号
     */
    private String criteria1;

    /**
     * 用户账户/委托编号/业务流水号
     */
    private String criteria2;

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
     * 始（用户创建时间）
     */
    private String startTime2;

    /**
     * 终（用户创建时间）
     */
    private String endTime2;

    @NotEmpty(groups = OrderIdGroup.class)
    private String orderId;

    /**
     * 主动方向（主动撮合的委托单方向）
     1：Buy
     2：Sell
     */
    private String activeSide;

    /**
     * 计价货币代码
     */
    private String quoteCurrency;

    private String sellBrokerName;

    private String buyBrokerName;

    public String getSellBrokerName () {
        return sellBrokerName;
    }

    public void setSellBrokerName ( String sellBrokerName ) {
        this.sellBrokerName = sellBrokerName;
    }

    public String getBuyBrokerName () {
        return buyBrokerName;
    }

    public void setBuyBrokerName ( String buyBrokerName ) {
        this.buyBrokerName = buyBrokerName;
    }

    public String getQuoteCurrency () {
        return quoteCurrency;
    }

    public void setQuoteCurrency ( String quoteCurrency ) {
        this.quoteCurrency = quoteCurrency;
    }

    public String getSymbol () {
        return symbol;
    }

    public void setSymbol ( String symbol ) {
        this.symbol = symbol;
    }

    public String getCriteria1 () {
        return criteria1;
    }

    public void setCriteria1 ( String criteria1 ) {
        this.criteria1 = criteria1;
    }

    public String getCriteria2 () {
        return criteria2;
    }

    public void setCriteria2 ( String criteria2 ) {
        this.criteria2 = criteria2;
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

    public String getOrderId () {
        return orderId;
    }

    public void setOrderId ( String orderId ) {
        this.orderId = orderId;
    }

    public String getActiveSide () {
        return activeSide;
    }

    public void setActiveSide ( String activeSide ) {
        this.activeSide = activeSide;
    }
}