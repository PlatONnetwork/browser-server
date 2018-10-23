package com.platon.browserweb.common.req.account;

import com.platon.browserweb.common.req.PageReq;

import javax.validation.constraints.NotNull;

/**
 * User: dongqile
 * Date: 2018/7/13
 * Time: 18:24
 */
public class CheckDetailReq extends PageReq {
    /**
     * 对账日期
     */
    @NotNull
    private long time;

    /**
     * 币种
     */
    private String currency;

    /**
     * 对账结果
     * 1：平
     2：不平
     3：海龟无记录
     4：海马无记录
     */
    private String status;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;


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

    public long getTime () {
        return time;
    }

    public void setTime ( long time ) {
        this.time = time;
    }

    public String getCurrency () {
        return currency;
    }

    public void setCurrency ( String currency ) {
        this.currency = currency;
    }

    public String getStatus () {
        return status;
    }

    public void setStatus ( String status ) {
        this.status = status;
    }
}