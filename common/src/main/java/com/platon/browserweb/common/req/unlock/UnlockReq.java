package com.platon.browserweb.common.req.unlock;

import com.platon.browserweb.common.req.PageReq;
import org.hibernate.validator.constraints.NotEmpty;


/**
 * User: dongqile
 * Date: 2018/8/27
 * Time: 10:03
 */
public class UnlockReq extends PageReq{
    /**
     * 查询条件--1：待处理,2：已处理
     */
    @NotEmpty
    private String queryStatus;

    /**
     * 用户账号
     */
    private String criteria;

    /**
     * 申请状态--1：审核通过,2：审核拒绝
     */
    private Integer status;

    /**
     * 证件类型
     */
    private String idType;

    /**
     * 国家
     */
    private String countryCode;

    /**
     * 用户账号
     */
    private Integer dateType = 30;

    /**
     * 开始时间
     */
    private long startTime;

    /**
     * 结束时间
     */
    private long endTime;

    private String startTime2;

    private String endTime2;

    private long auditId;

    public long getAuditId () {
        return auditId;
    }

    public void setAuditId ( long auditId ) {
        this.auditId = auditId;
    }

    public String getQueryStatus () {
        return queryStatus;
    }

    public void setQueryStatus ( String queryStatus ) {
        this.queryStatus = queryStatus;
    }

    public String getCriteria () {
        return criteria;
    }

    public void setCriteria ( String criteria ) {
        this.criteria = criteria;
    }

    public Integer getStatus () {
        return status;
    }

    public void setStatus ( Integer status ) {
        this.status = status;
    }

    public String getIdType () {
        return idType;
    }

    public void setIdType ( String idType ) {
        this.idType = idType;
    }

    public String getCountryCode () {
        return countryCode;
    }

    public void setCountryCode ( String countryCode ) {
        this.countryCode = countryCode;
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