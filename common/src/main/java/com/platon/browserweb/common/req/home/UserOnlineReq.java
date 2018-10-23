package com.platon.browserweb.common.req.home;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * User: dongqile
 * Date: 2018/9/3
 * Time: 16:49
 */
public class UserOnlineReq {
    /**
     * 更新时间（近期用户登陆传单日时间）(必传字段)
     */
    @NotNull
    private Date updateTime;

    /**
     * 列表类型
     * realTime: 实时
     * recent：近期
     */
    @NotEmpty
    private String type;

    private String startTime;

    private String endTime;

    private long brokerId;


    public long getBrokerId () {
        return brokerId;
    }

    public void setBrokerId ( long brokerId ) {
        this.brokerId = brokerId;
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

    public Date getUpdateTime () {
        return updateTime;
    }

    public void setUpdateTime ( Date updateTime ) {
        this.updateTime = updateTime;
    }

    public String getType () {
        return type;
    }

    public void setType ( String type ) {
        this.type = type;
    }
}