package com.platon.browser.response.address;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.platon.browser.config.json.CustomLatSerializer;

import java.math.BigDecimal;
import java.util.List;

/**
 * 查询地址锁仓信息的返回的对象
 *
 * @author zhangrj
 * @file QueryRPPlanDetailResp.java
 * @description
 * @data 2019年8月31日
 */
public class QueryRPPlanDetailResp {

    /**
     * 锁仓余额(单位:ATP)
     */
    private BigDecimal restrictingBalance;

    /**
     * 锁仓质押\委托(单位:ATP)
     */
    private BigDecimal stakingValue;

    /**
     * 欠释放(单位:ATP)
     */
    private BigDecimal underReleaseValue;

    /**
     * 锁仓计划
     */
    private List<DetailsRPPlanResp> rpPlans;

    private Long total;

    /**
     * 总计锁仓
     */
    private BigDecimal totalValue;

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getRestrictingBalance() {
        return restrictingBalance;
    }

    public void setRestrictingBalance(BigDecimal restrictingBalance) {
        this.restrictingBalance = restrictingBalance;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getStakingValue() {
        return stakingValue;
    }

    public void setStakingValue(BigDecimal stakingValue) {
        this.stakingValue = stakingValue;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getUnderReleaseValue() {
        return underReleaseValue;
    }

    public void setUnderReleaseValue(BigDecimal underReleaseValue) {
        this.underReleaseValue = underReleaseValue;
    }

    public List<DetailsRPPlanResp> getRpPlans() {
        return rpPlans;
    }

    public void setRpPlans(List<DetailsRPPlanResp> rpPlans) {
        this.rpPlans = rpPlans;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    @JsonSerialize(using = CustomLatSerializer.class)
    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

}
