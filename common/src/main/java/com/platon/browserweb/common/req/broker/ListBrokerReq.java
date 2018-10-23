package com.platon.browserweb.common.req.broker;

import com.platon.browserweb.common.req.PageReq;
import com.platon.browserweb.common.validate.CategoryGroup;
import com.platon.browserweb.common.validate.IdGroup;
import com.platon.browserweb.common.validate.IfPledgedGroup;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * User: dongqile
 * Date: 2018/7/2
 * Time: 11:33
 */
public class ListBrokerReq extends PageReq {


    /**
     * 类型
     COMPANY：企业
     PERSONAL：个人
     */
    @NotEmpty(groups = CategoryGroup.class)
    private String category;


    /**
     * 保证金
     1：已缴纳
     2：未缴纳
     */
    private String pledged;


    /**
     * 状态
     1：正常
     2：冻结
     */
    private String status;


    /**
     * 状态
     1：正常
     2：冻结
     */
    private String accountStatus;


    /**
     * 代理商id
     */
    private String brokerId;


    /**
     * 交易所id
     */
    private String exchangeId;


    /**
     * 用户类型
     */
    private String type;


    /**
     * id
     */
    @NotNull(groups = IdGroup.class)
    private long id;

    /**
     * 是否已经缴纳保证金
     false：否
     true：是
     */
    @NotNull(groups = IfPledgedGroup.class)
    private boolean ifPledged;

    private Integer juwebId;

    private String userId;

    /**
     * 代理商名称
     */
    private String name;

    /**
     * 代理商email
     */
    private String email;
    /**
     * 加盟日期
     */
    private long joinDate;

    /**
     * 语言
     */
    private String lang;

    private String juwebName;

    public String getJuwebName () {
        return juwebName;
    }

    public void setJuwebName ( String juwebName ) {
        this.juwebName = juwebName;
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

    public long getJoinDate () {
        return joinDate;
    }

    public void setJoinDate ( long joinDate ) {
        this.joinDate = joinDate;
    }

    public Integer getJuwebId () {
        return juwebId;
    }

    public void setJuwebId ( Integer juwebId ) {
        this.juwebId = juwebId;
    }

    public String getUserId () {
        return userId;
    }

    public void setUserId ( String userId ) {
        this.userId = userId;
    }



    public String getCategory () {
        return category;
    }

    public void setCategory ( String category ) {
        this.category = category;
    }

    public String getPledged () {
        return pledged;
    }

    public String getType () {
        return type;
    }

    public void setType ( String type ) {
        this.type = type;
    }

    public void setPledged ( String pledged ) {
        this.pledged = pledged;
    }

    public String getStatus () {
        return status;
    }

    public void setStatus ( String status ) {
        this.status = status;
    }

    public String getAccountStatus () {
        return accountStatus;
    }

    public void setAccountStatus ( String accountStatus ) {
        this.accountStatus = accountStatus;
    }

    public String getBrokerId () {
        return brokerId;
    }

    public void setBrokerId ( String brokerId ) {
        this.brokerId = brokerId;
    }

    public String getExchangeId () {
        return exchangeId;
    }

    public void setExchangeId ( String exchangeId ) {
        this.exchangeId = exchangeId;
    }

    public long getId () {
        return id;
    }

    public void setId ( long id ) {
        this.id = id;
    }

    public boolean isIfPledged () {
        return ifPledged;
    }

    public void setIfPledged ( boolean ifPledged ) {
        this.ifPledged = ifPledged;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}