package com.platon.browserweb.common.req.account;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * User: dongqile
 * Date: 2018/7/10
 * Time: 11:54
 */
public class BrokerWithdrawReq {
    /**
     * 币种
     */
    @NotEmpty
    private String currency;

    /**
     *  提币地址
     */
    @NotEmpty
    private String toAddress;


    /**
     * 提币数量
     */
    @NotEmpty
    private String orderQty;


    /**
     * 邮箱验证码
     */
    @NotEmpty
    private String mailValidCode;

    private long userId;

    private Integer juwebId;

    private String juwebName;

    public Integer getJuwebId () {
        return juwebId;
    }

    public void setJuwebId ( Integer juwebId ) {
        this.juwebId = juwebId;
    }

    public String getJuwebName () {
        return juwebName;
    }

    public void setJuwebName ( String juwebName ) {
        this.juwebName = juwebName;
    }

    public String getCurrency () {
        return currency;
    }

    public void setCurrency ( String currency ) {
        this.currency = currency;
    }

    public String getToAddress () {
        return toAddress;
    }

    public void setToAddress ( String toAddress ) {
        this.toAddress = toAddress;
    }

    public String getOrderQty () {
        return orderQty;
    }

    public void setOrderQty ( String orderQty ) {
        this.orderQty = orderQty;
    }

    public String getMailValidCode () {
        return mailValidCode;
    }

    public void setMailValidCode ( String mailValidCode ) {
        this.mailValidCode = mailValidCode;
    }

    public long getUserId () {
        return userId;
    }

    public void setUserId ( long userId ) {
        this.userId = userId;
    }
}