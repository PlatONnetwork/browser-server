package com.platon.browserweb.common.req.account;

import com.platon.browserweb.common.req.PageReq;

/**
 * User: dongqile
 * Date: 2018/7/9
 * Time: 20:44
 */
public class ClearingReportReq extends PageReq{
    /**
     * 币种
     */
    private String currency;

    /**
     * 业务代码
     *     DEPOSIT：充币
     WITHDRAW：提币
     BID：买
     ASK：卖
     */
    private String bizCode;
    /**
     * 用户分类
     *     INVESTOR：投资者
     BROKER：代理商
     EXCHANGE：交易所
     */
    private String userType;

    /**
     * 投资者名称/代理商名称/业务流水号
     */
    private String  criteria;
    /**
     * 查询时间
     */
    private Integer dateType = 30;

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

    private String type;

    private long userId;

    private String base;

    private String quote;

    public String getBase () {
        return base;
    }

    public void setBase ( String base ) {
        this.base = base;
    }

    public String getQuote () {
        return quote;
    }

    public void setQuote ( String quote ) {
        this.quote = quote;
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

    public String getCurrency () {
        return currency;
    }

    public void setCurrency ( String currency ) {
        this.currency = currency;
    }

    public String getBizCode () {
        return bizCode;
    }

    public void setBizCode ( String bizCode ) {
        this.bizCode = bizCode;
    }

    public String getUserType () {
        return userType;
    }

    public void setUserType ( String userType ) {
        this.userType = userType;
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

    public long getUserId () {
        return userId;
    }

    public void setUserId ( long userId ) {
        this.userId = userId;
    }
}