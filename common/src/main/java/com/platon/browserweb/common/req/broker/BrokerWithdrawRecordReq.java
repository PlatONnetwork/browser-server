package com.platon.browserweb.common.req.broker;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * User: dongqile
 * Date: 2018/8/31
 * Time: 11:33
 */
public class BrokerWithdrawRecordReq {

    /**
     *  币种
     */
    @NotEmpty
    private String currency;

    /**
     *  状态
     */
    private String ordStatus;

    /**
     *  操作用户
     */
    private String criteria1;

    /**
     * 业务流水/交易hash/提币地址
     */
    private String criteria2;

    private  long  brokerId;

    public long getBrokerId () {
        return brokerId;
    }

    public void setBrokerId ( long brokerId ) {
        this.brokerId = brokerId;
    }

    public String getOrdStatus () {
        return ordStatus;
    }

    public void setOrdStatus ( String ordStatus ) {
        this.ordStatus = ordStatus;
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

    public String getCurrency () {
        return currency;
    }

    public void setCurrency ( String currency ) {
        this.currency = currency;
    }
}