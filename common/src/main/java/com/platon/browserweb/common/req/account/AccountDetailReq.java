package com.platon.browserweb.common.req.account;

import com.platon.browserweb.common.req.PageReq;

/**
 * User: dongqile
 * Date: 2018/7/9
 * Time: 11:12
 */
public class AccountDetailReq extends PageReq {
    /**
     * 收/支
     1：支出
     2：收益
     */
    private String income;

    /**
     * 币种
     */
    private String currency;

    /**
     * 业务类型
     INVESTOR_WITHDRAW_PROFIT：投资者提币分润
     INVESTOR_BID_PROFIT：投资者买入交易分润
     INVESTOR_ASK_PROFIT：投资者卖出交易分润
     BROKER_WITHDRAW_PROFIT：代理商提币分润
     */
    private String ledgerItem;

    /**
     * 查询时间
     */
    private Integer dateType = 7;

    /**
     * 开始时间
     */
    private long startTime;

    /**
     * 结束时间
     */
    private long endTime;

    /**
     * 开始时间
     */
    private String startTime2;

    /**
     * 结束时间
     */
    private String endTime2;

    public String getIncome () {
        return income;
    }

    public void setIncome ( String income ) {
        this.income = income;
    }

    public String getCurrency () {
        return currency;
    }

    public void setCurrency ( String currency ) {
        this.currency = currency;
    }

    public String getLedgerItem () {
        return ledgerItem;
    }

    public void setLedgerItem ( String ledgerItem ) {
        this.ledgerItem = ledgerItem;
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