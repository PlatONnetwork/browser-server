package com.platon.browserweb.common.req.investor;

import com.platon.browserweb.common.req.PageReq;

/**
 * User: dongqile
 * Date: 2018/7/4
 * Time: 16:41
 */
public class ListInvestorReq extends PageReq {

    /**
     * 用户状态
     1：正常
     2：冻结
     */
    private String status;

    /**
     * 账户状态
     1：正常
     2：冻结
     */
    private String accountStatus;

    /**
     * 信用等级
     */
    private String userLevelCode;

    /**
     * 认证类型
     1：个人认证
     2：企业认证
     */
    private String kycAuditType;

    /**
     * 证件类型
     1：身份证
     2：营业执照
     */
    private String idType;

    /**
     * 证件号码
     */
    private String idNo;

    /**
     * 代理商名称
     */
    private String brokerName;

    /**
     * 国家代码
     */
    private String countryCode;

    /**
     * 用户账户/用户手机号/用户姓名
     */
    private String criteria;

    /**
     * 查询时间
     */
    private Integer dateType = 30;

    /**
     * 始（用户创建时间）
     */
    private long startTime;

    /**
     * 终（用户创建时间）
     */
    private long endTime;


    /**
     * 用户分类
     INVESTOR：投资者
     BROKER：代理商
     EXCHANGE：交易所
     */
    private String type;

    /**
     * 用户等级分数区间（最小值）
     */
    private Integer rankingMin;

    /**
     * 用户等级分数区间（最大值）
     */
    private Integer rankingMax;

    private long exchangeId;

    /**
     * 始（用户创建时间）
     */
    private String startTime2;

    /**
     * 终（用户创建时间）
     */
    private String endTime2;


    private String userId;


    private long brokerId;

    public long getBrokerId () {
        return brokerId;
    }

    public void setBrokerId ( long brokerId ) {
        this.brokerId = brokerId;
    }

    public String getUserId () {
        return userId;
    }

    public void setUserId ( String userId ) {
        this.userId = userId;
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

    public String getUserLevelCode () {
        return userLevelCode;
    }

    public void setUserLevelCode ( String userLevelCode ) {
        this.userLevelCode = userLevelCode;
    }

    public String getKycAuditType () {
        return kycAuditType;
    }

    public void setKycAuditType ( String kycAuditType ) {
        this.kycAuditType = kycAuditType;
    }

    public String getIdType () {
        return idType;
    }

    public void setIdType ( String idType ) {
        this.idType = idType;
    }

    public String getIdNo () {
        return idNo;
    }

    public void setIdNo ( String idNo ) {
        this.idNo = idNo;
    }

    public String getBrokerName () {
        return brokerName;
    }

    public void setBrokerName ( String brokerName ) {
        this.brokerName = brokerName;
    }

    public String getCountryCode () {
        return countryCode;
    }

    public void setCountryCode ( String countryCode ) {
        this.countryCode = countryCode;
    }

    public String getCriteria () {
        return criteria;
    }

    public void setCriteria ( String criteria ) {
        this.criteria = criteria;
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

    public String getType () {
        return type;
    }

    public void setType ( String type ) {
        this.type = type;
    }

    public Integer getRankingMin () {
        return rankingMin;
    }

    public void setRankingMin ( Integer rankingMin ) {
        this.rankingMin = rankingMin;
    }

    public Integer getRankingMax () {
        return rankingMax;
    }

    public void setRankingMax ( Integer rankingMax ) {
        this.rankingMax = rankingMax;
    }

    public long getExchangeId () {
        return exchangeId;
    }

    public void setExchangeId ( long exchangeId ) {
        this.exchangeId = exchangeId;
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