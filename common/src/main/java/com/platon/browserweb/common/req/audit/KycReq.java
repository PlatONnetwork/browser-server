package com.platon.browserweb.common.req.audit;


import com.platon.browserweb.common.req.PageReq;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * User: dongqile
 * Date: 2018/6/27
 * Time: 15:59
 */
public class KycReq extends PageReq {


    /**
     * 查询条件
     1：待处理
     2：已处理
     */
    @NotEmpty
    private String queryStatus;

    /**
     * 国家
     */
    private String countryCode;

    /**
     * 证件类型:
     * 1：身份证
     * 2：营业执照
     */
    private Integer idType;

    /**
     * 用户账号\姓名
     * 机构名称\用户账号
     */
    private String criteria ;

    /**
     * 申请状态
     * 1：审核通过
     * 2：审核拒绝
     */
    private String orderStatus;

    /**
     * 查询时间
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

    /**
     * 类型
     */
    private String type;

    /**
     * 币种
     */
    private String currency;

    /**
     * 代理商id
     */
    private long brokerId;

    /**
     * 交易所id
     */
    private long exchangeId;

    /**
     * 开始时间
     */
    private String startTime2;

    /**
     * 结束时间
     */
    private String endTime2;

    private Integer kycType;



    /**
     * 开始时间
     */
    private String startTime3;

    /**
     * 结束时间
     */
    private String endTime3;


    private String name;

    public String getName () {
        return name;
    }

    public void setName ( String name ) {
        this.name = name;
    }

    public String getStartTime3 () {
        return startTime3;
    }

    public void setStartTime3 ( String startTime3 ) {
        this.startTime3 = startTime3;
    }

    public String getEndTime3 () {
        return endTime3;
    }

    public void setEndTime3 ( String endTime3 ) {
        this.endTime3 = endTime3;
    }


    public Integer getKycType () {
        return kycType;
    }

    public void setKycType ( Integer kycType ) {
        this.kycType = kycType;
    }

    public String getType () {
        return type;
    }

    public void setType ( String type ) {
        this.type = type;
    }

    public String getQueryStatus () {
        return queryStatus;
    }

    public void setQueryStatus ( String queryStatus ) {
        this.queryStatus = queryStatus;
    }

    public String getCountryCode () {
        return countryCode;
    }

    public void setCountryCode ( String countryCode ) {
        this.countryCode = countryCode;
    }

    public Integer getIdType () {
        return idType;
    }

    public void setIdType ( Integer idType ) {
        this.idType = idType;
    }

    public String getCriteria () {
        return criteria;
    }

    public void setCriteria ( String criteria ) {
        this.criteria = criteria;
    }

    public String getOrderStatus () {
        return orderStatus;
    }

    public void setOrderStatus ( String orderStatus ) {
        this.orderStatus = orderStatus;
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

    public Integer getDateType () {
        return dateType;
    }

    public void setDateType ( Integer dateType ) {
        this.dateType = dateType;
    }

    public String getCurrency () {
        return currency;
    }

    public void setCurrency ( String currency ) {
        this.currency = currency;
    }

    public long getBrokerId () {
        return brokerId;
    }

    public void setBrokerId ( long brokerId ) {
        this.brokerId = brokerId;
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