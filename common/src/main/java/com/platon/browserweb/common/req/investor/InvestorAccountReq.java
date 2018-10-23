package com.platon.browserweb.common.req.investor;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * User: dongqile
 * Date: 2018/8/21
 * Time: 11:48
 */
public class InvestorAccountReq {
    /**
     * 用户账号\手机
     */
    @NotEmpty
    private String criteria;

    private String type;

    public String getType () {
        return type;
    }

    public void setType ( String type ) {
        this.type = type;
    }

    public String getCriteria () {
        return criteria;
    }

    public void setCriteria ( String criteria ) {
        this.criteria = criteria;
    }
}