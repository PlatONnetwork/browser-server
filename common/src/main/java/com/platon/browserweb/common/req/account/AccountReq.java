package com.platon.browserweb.common.req.account;

import com.platon.browserweb.common.req.PageReq;

/**
 * User: dongqile
 * Date: 2018/7/10
 * Time: 11:02
 */
public class AccountReq extends PageReq{

    /**
     * 币种
     */
    private String currency;

    private long time;

    private long userId;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;


    public String getCurrency () {
        return currency;
    }

    public void setCurrency ( String currency ) {
        this.currency = currency;
    }

    public long getTime () {
        return time;
    }

    public void setTime ( long time ) {
        this.time = time;
    }

    public long getUserId () {
        return userId;
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


    public void setUserId ( long userId ) {
        this.userId = userId;
    }
}