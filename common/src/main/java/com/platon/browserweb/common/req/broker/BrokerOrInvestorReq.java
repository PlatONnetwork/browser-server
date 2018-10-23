package com.platon.browserweb.common.req.broker;

import com.platon.browserweb.common.req.PageReq;
import com.platon.browserweb.common.validate.AccountStatusGroup;
import com.platon.browserweb.common.validate.ReasonGroup;
import com.platon.browserweb.common.validate.StatusGroup;
import com.platon.browserweb.common.validate.UserIdGroup;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * User: dongqile
 * Date: 2018/7/2
 * Time: 17:49
 */
public class BrokerOrInvestorReq extends PageReq {
    /**
     * 代理商id
     */
    @NotNull(groups = UserIdGroup.class)
    private long userId;

    /**
     * 状态
     0：正常
     1：冻结
     */
    @NotNull(groups = StatusGroup.class)
    private Integer status;

    /**
     * 理由
     */
    @NotEmpty(groups = ReasonGroup.class)
    private  String reason;

    /**
     * 状态
     0：正常
     1：冻结
     */
    @NotNull(groups = AccountStatusGroup.class)
    private Integer accountStatus;

    private Integer juwebId;

    private String juwebName;

    public String getJuwebName () {
        return juwebName;
    }

    public void setJuwebName ( String juwebName ) {
        this.juwebName = juwebName;
    }

    public Integer getJuwebId () {
        return juwebId;
    }

    public void setJuwebId ( Integer juwebId ) {
        this.juwebId = juwebId;
    }

    public long getUserId () {
        return userId;
    }

    public void setUserId ( long userId ) {
        this.userId = userId;
    }

    public Integer getStatus () {
        return status;
    }

    public void setStatus ( Integer status ) {
        this.status = status;
    }

    public String getReason () {
        return reason;
    }

    public void setReason ( String reason ) {
        this.reason = reason;
    }

    public Integer getAccountStatus () {
        return accountStatus;
    }

    public void setAccountStatus ( Integer accountStatus ) {
        this.accountStatus = accountStatus;
    }
}