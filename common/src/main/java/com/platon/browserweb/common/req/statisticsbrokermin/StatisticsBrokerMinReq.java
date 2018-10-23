package com.platon.browserweb.common.req.statisticsbrokermin;

/**
 * User: dongqile
 * Date: 2018/9/4
 * Time: 11:00
 */
public class StatisticsBrokerMinReq {

    /**
     *  表名
     */
    private String tableName;

    /**
     *  代理商id
     */
    private long brokerId;

    /**
     *  类型
     */
    private String type;

    /**
     *  币种
     */
    private String currency;

    /**
     *  币对
     */
    private String symbol;

    /**
     *  买卖方向
     */
    private String side;

    public String getTableName () {
        return tableName;
    }

    public void setTableName ( String tableName ) {
        this.tableName = tableName;
    }

    public long getBrokerId () {
        return brokerId;
    }

    public void setBrokerId ( long brokerId ) {
        this.brokerId = brokerId;
    }

    public String getType () {
        return type;
    }

    public void setType ( String type ) {
        this.type = type;
    }

    public String getCurrency () {
        return currency;
    }

    public void setCurrency ( String currency ) {
        this.currency = currency;
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
}