package com.platon.browserweb.common.req.broker;

import com.platon.browserweb.common.req.PageReq;

/**
 * User: dongqile
 * Date: 2018/7/4
 * Time: 14:45
 */
public class BrokerAccountDetailReq extends PageReq {

    /**
     * 收/支
     1：支出
     2：收益
     */
    private String income;

    /**
     * 收支项目
     INVESTOR_WITHDRAW_PROFIT：投资者提币分润
     INVESTOR_BID_PROFIT：投资者买入交易分润
     INVESTOR_ASK_PROFIT：投资者卖出交易分润
     BROKER_WITHDRAW：代理商提币
     */
    private  String ledgerItem;

    /**
     * 币种
     */
    private String currency;

    /**
     * 代理商名称/订单id
     */
    private String criteria;

    /**
     * 查询时间
     */
    private Integer dateType = 30;

    private long startTime;

    private long endTime;


    private String startTime2;

    private String endTime2;

    private long exchangeId;

    private String brokerName;

    public String getBrokerName () {
        return brokerName;
    }

    public void setBrokerName ( String brokerName ) {
        this.brokerName = brokerName;
    }

    public long getExchangeId () {
        return exchangeId;
    }

    public void setExchangeId ( long exchangeId ) {
        this.exchangeId = exchangeId;
    }

    public String getIncome () {
        return income;
    }

    public void setIncome ( String income ) {
        this.income = income;
    }

    public String getLedgerItem () {
        return ledgerItem;
    }

    public void setLedgerItem ( String ledgerItem ) {
        this.ledgerItem = ledgerItem;
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