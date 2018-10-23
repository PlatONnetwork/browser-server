/*
 * Copyright (c) 2018. juzhen.io. All rights reserved.
 */

package com.platon.browserweb.common.req;

import com.github.fartherp.framework.common.validate.Value;
import com.platon.browserweb.common.validate.ValidCodeGroup;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * User: dongqile
 * Date: 2018/6/25
 * Time: 11:15
 */
public class ValidCodeReq {

    /**
     * Withdraw:提款提币
     * AddWithdrawAddress:添加提币地址
     */
    @Value(values = {"withdraw", "addWalletUrl"})
    private String type;

    /**
     * 1：短信方式
     * 2：邮件方式
     */
    @NotNull
    @Value(values = {"1", "2"})
    private Integer sendMode;

    /**
     * 收件人地址(手机或者邮箱)
     */
    @NotEmpty
    private String address;
    /**
     * 验证码
     */
    @NotEmpty(groups = ValidCodeGroup.class)
    private String validCode;

    /**
     * rediskey前缀
     */
    private String prefix = "ADMIN_";

    private String subject;

    public String getSubject () {
        return subject;
    }

    public void setSubject ( String subject ) {
        this.subject = subject;
    }

    public String getPrefix () {
        return prefix;
    }

    public void setPrefix ( String prefix ) {
        this.prefix = prefix;
    }

    public String getType () {
        return type;
    }

    public void setType ( String type ) {
        this.type = type;
    }

    public Integer getSendMode () {
        return sendMode;
    }

    public void setSendMode ( Integer sendMode ) {
        this.sendMode = sendMode;
    }

    public String getAddress () {
        return address;
    }

    public void setAddress ( String address ) {
        this.address = address;
    }

    public String getValidCode () {
        return validCode;
    }

    public void setValidCode ( String validCode ) {
        this.validCode = validCode;
    }
}
