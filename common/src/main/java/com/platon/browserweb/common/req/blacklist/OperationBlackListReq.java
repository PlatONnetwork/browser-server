package com.platon.browserweb.common.req.blacklist;

import com.github.fartherp.framework.common.validate.Value;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;


/**
 * User: dongqile
 * Date: 2018/8/21
 * Time: 18:09
 */
public class OperationBlackListReq {
    /**
     * 用户id
     */
    @NotNull
    private long userId;

    /**
     * 操作
     * 0：拉入黑名单
     * 1：移除黑名单
     */
    @NotNull
    @Value(values = {"0", "1"})
    private Integer operation;

    /**
     * 拉入/移除理由
     */
    @NotEmpty
    private String reason;


    /**
     * 黑名单类型 1：交易 2：提币
     */
    @NotEmpty
    @Value(values = {"WITHDRAW", "TRADE"})
    private String type;


    private String juwebName;

    private Integer juwebId;

    public Integer getOperation () {
        return operation;
    }

    public void setOperation ( Integer operation ) {
        this.operation = operation;
    }

    public String getReason () {
        return reason;
    }

    public void setReason ( String reason ) {
        this.reason = reason;
    }

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
}