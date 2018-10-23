package com.platon.browserweb.common.req.blacklist;

import com.github.fartherp.framework.common.validate.Value;
import com.platon.browserweb.common.req.PageReq;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * User: dongqile
 * Date: 2018/8/21
 * Time: 15:19
 */
public class BlackListReq extends PageReq {

    /**
     * 黑名单类型 1：交易 2：提币
     */
    @NotEmpty
    @Value(values = {"WITHDRAW", "TRADE"})
    private String type;

    /**
     * 查询条件--0：交易黑名单,1：历史黑名单
     */
    private Integer queryStatus;

    /**
     * 用户账号\证件号\手机\姓名
     */
    private String criteria;

    /**
     * 查询时间
     */
    private Integer dateType =30;

    /**
     * 始
     */
    private long startTime;

    /**
     * 终
     */
    private long endTime;

    private String startTime2;

    private String endTime2;

    private long userId;


    public long getUserId () {
        return userId;
    }

    public void setUserId ( long userId ) {
        this.userId = userId;
    }

    public String getType () {
        return type;
    }

    public void setType ( String type ) {
        this.type = type;
    }

    public Integer getQueryStatus () {
        return queryStatus;
    }

    public void setQueryStatus ( Integer queryStatus ) {
        this.queryStatus = queryStatus;
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