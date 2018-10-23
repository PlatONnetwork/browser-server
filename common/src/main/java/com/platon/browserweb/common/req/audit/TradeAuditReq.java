package com.platon.browserweb.common.req.audit;

import com.platon.browserweb.common.req.PageReq;

import javax.validation.constraints.NotNull;

/**
 * User: dongqile
 * Date: 2018/6/28
 * Time: 15:12
 */
public class TradeAuditReq extends PageReq{


    /**
     * 查询条件
     1：待处理
     2：已处理
     */
    @NotNull
    private Integer queryStatus;

    /**
     * 币对  BTC_ETH
     */
    private String symbol;

    /**
     * 方向
     1：Buy
     2：Sell
     */
    private String side;

    /**
     * 委托报价方式
     MARKET：市价单
     LIMIT：现价单
     AMOUNT_MARKET：市价金额单
     AMOUNT_LIMIT：现价金额单
     */
    private String orderType;

    /**
     * 委托有效期规则
     1：Good Till Cancel (GTC)
     3：Immediate or Cancel (IOC)
     6：Good Till Date (GTD)
     7：Good Till Range (GTR)
     */
    private String timeInForce;

    /**
     * 姓名\用户账号
     */
    private String criteria;

    /**
     * 申请状态
     * 1：审核通过
     * 2：审核拒绝
     */
    private String orderStatus;

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
     * 代理商id
     */
    private long brokerId;

    /**
     * 开始时间
     */
    private String startTime2;

    /**
     * 结束时间
     */
    private String endTime2;


    /**
     * 开始时间
     */
    private String startTime3;

    /**
     * 结束时间
     */
    private String endTime3;

    public String getStartTime3 () {
        return startTime3;
    }

    public void setStartTime3 ( String startTime3 ) {
        this.startTime3 = startTime3;
    }

    public String getEndTime3 () {
        return endTime3;
    }

    public void setEndTime3 ( String endTime3 ) {
        this.endTime3 = endTime3;
    }

    public Integer getQueryStatus () {
        return queryStatus;
    }

    public void setQueryStatus ( Integer queryStatus ) {
        this.queryStatus = queryStatus;
    }

    public String getSymbol () {
        return symbol;
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

    public String getOrderType () {
        return orderType;
    }

    public void setOrderType ( String orderType ) {
        this.orderType = orderType;
    }

    public String getTimeInForce () {
        return timeInForce;
    }

    public void setTimeInForce ( String timeInForce ) {
        this.timeInForce = timeInForce;
    }

    public String getCriteria () {
        return criteria;
    }

    public void setCriteria ( String criteria ) {
        this.criteria = criteria;
    }

    public String getOrderStatus () {
        return orderStatus;
    }

    public void setOrderStatus ( String orderStatus ) {
        this.orderStatus = orderStatus;
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