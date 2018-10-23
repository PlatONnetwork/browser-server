package com.platon.browserweb.common.req.home;

import com.platon.browserweb.common.validate.QuoteCurrencyGroup;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * User: dongqile
 * Date: 2018/9/6
 * Time: 10:06
 */
public class RateTrendReq {

    private long brokerId;

    @NotEmpty(groups = QuoteCurrencyGroup.class)
    private String queryStatus;

    private Integer dateType = 30;

    private String startTime;

    private String endTime;

    private String type;

    public String getType () {
        return type;
    }

    public void setType ( String type ) {
        this.type = type;
    }

    public long getBrokerId () {
        return brokerId;
    }

    public void setBrokerId ( long brokerId ) {
        this.brokerId = brokerId;
    }

    public String getQueryStatus () {
        return queryStatus;
    }

    public void setQueryStatus ( String queryStatus ) {
        this.queryStatus = queryStatus;
    }

    public Integer getDateType () {
        return dateType;
    }

    public void setDateType ( Integer dateType ) {
        this.dateType = dateType;
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
}