package com.platon.browserweb.common.req.account;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * User: dongqile
 * Date: 2018/7/10
 * Time: 10:59
 */
public class WithdrawAddressReq {

    /**
     * 币种
     */
    @NotEmpty
    private String currency;

    /**
     * 地址
     */
    @NotEmpty
    private String address;


    /**
     * 地址标签
     */
    @NotEmpty
    private String tag;


    /**
     * 邮箱验证码
     */
    @NotEmpty
    private String mailValidCode;

    private long userId;

    public String getCurrency () {
        return currency;
    }

    public void setCurrency ( String currency ) {
        this.currency = currency;
    }

    public String getAddress () {
        return address;
    }

    public void setAddress ( String address ) {
        this.address = address;
    }

    public String getTag () {
        return tag;
    }

    public void setTag ( String tag ) {
        this.tag = tag;
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