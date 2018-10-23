package com.platon.browserweb.common.req.home;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * User: dongqile
 * Date: 2018/6/27
 * Time: 14:33
 */
public class TrendReq {
    /**
     *  始（时间）
     */
    private String startTime;

    /**
     *  终（时间）
     */
    private String endTime;

    /**
     *  币种（例：BTC,ETH等...）
     */
    @NotEmpty
    private String currency;

    /**
     *  代理商id
     */
    private long brokerId;


    /**
     * 默认近一个月
     */
    private Integer dateType = 30;

    /**
     * 计价币代码
     */
    private String quoteCurrency;


    /**
     * 类型
     ACCOUNT：投资者账户总额
     REGISTER：投资者注册数
     ORDER_QTY：投资者委托量
     TRADE_QTY：成交量
     */
    private String type;

    private String side;

    private String symbol;

    private String flag;

    public String getFlag () {
        return flag;
    }

    public void setFlag ( String flag ) {
        this.flag = flag;
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

    public String getCurrency () {
        return currency;
    }

    public void setCurrency ( String currency ) {
        this.currency = currency;
    }

    public long getBrokerId () {
        return brokerId;
    }

    public void setBrokerId ( long brokerId ) {
        this.brokerId = brokerId;
    }

    public Integer getDateType () {
        return dateType;
    }

    public void setDateType ( Integer dateType ) {
        this.dateType = dateType;
    }

    public String getQuoteCurrency () {
        return quoteCurrency;
    }

    public void setQuoteCurrency ( String quoteCurrency ) {
        this.quoteCurrency = quoteCurrency;
    }

    public String getType () {
        return type;
    }

    public void setType ( String type ) {
        this.type = type;
    }
}