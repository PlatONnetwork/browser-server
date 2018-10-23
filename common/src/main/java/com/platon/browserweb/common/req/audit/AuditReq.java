package com.platon.browserweb.common.req.audit;

import javax.validation.constraints.NotNull;

/**
 * User: dongqile
 * Date: 2018/6/27
 * Time: 21:15
 */
public class AuditReq {
    /**
     * 认证记录id
     */
    @NotNull
    private Integer id;

    /**
     * 操作：
     * 1：同意
     * 2：拒绝
     */
    @NotNull
    private Integer operation;

    /**
     * 审核失败备注
     */
    private String auditRemark;

    private String juwebName;

    private Integer juwebId;


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

    public Integer getId () {
        return id;
    }

    public void setId ( Integer id ) {
        this.id = id;
    }

    public String getAuditRemark () {
        return auditRemark;
    }

    public void setAuditRemark ( String auditRemark ) {
        this.auditRemark = auditRemark;
    }

    public Integer getOperation () {
        return operation;
    }

    public void setOperation ( Integer operation ) {
        this.operation = operation;
    }

}