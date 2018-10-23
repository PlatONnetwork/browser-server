package com.platon.browserweb.common.req.broker;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * User: dongqile
 * Date: 2018/7/2
 * Time: 19:39
 */
public class NewBrokerReq {

    /**
     * 代理商名称
     */
    @NotEmpty
    private String name;

    /**
     * 代理商邮箱
     */
    @NotEmpty
    private String email;

    /**
     * 类型
     COMPANY：企业
     PERSONAL：个人
     */
    @NotEmpty
    private String category;

    /**
     * 是否已经缴纳保证金
     false：否
     true：是
     */
    @NotNull
    private boolean ifPledged;

    /**
     * 加盟日期
     */
    @NotNull
    private long joinDate;

    /**
     * 语言
     */
    @NotEmpty
    private String lang;

    public String getLang () {
        return lang;
    }

    public void setLang ( String lang ) {
        this.lang = lang;
    }

    private Integer juwebId;

    private long userId;

    private String juwebName;

    public String getJuwebName () {
        return juwebName;
    }

    public void setJuwebName ( String juwebName ) {
        this.juwebName = juwebName;
    }

    public long getUserId () {
        return userId;
    }

    public void setUserId ( long userId ) {
        this.userId = userId;
    }

    public Integer getJuwebId () {
        return juwebId;
    }

    public void setJuwebId ( Integer juwebId ) {
        this.juwebId = juwebId;
    }

    public String getName () {
        return name;
    }

    public void setName ( String name ) {
        this.name = name;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail ( String email ) {
        this.email = email;
    }

    public String getCategory () {
        return category;
    }

    public void setCategory ( String category ) {
        this.category = category;
    }

    public boolean isIfPledged () {
        return ifPledged;
    }

    public void setIfPledged ( boolean ifPledged ) {
        this.ifPledged = ifPledged;
    }

    public long getJoinDate () {
        return joinDate;
    }

    public void setJoinDate ( long joinDate ) {
        this.joinDate = joinDate;
    }
}