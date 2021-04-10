package com.platon.browser.bean;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/21 15:09
 * @Description: 年化率信息bean
 */
public class AnnualizedRateInfo {
    private List<PeriodValueElement> stakeProfit;
    private List<PeriodValueElement> stakeCost;
    private List<PeriodValueElement> delegateProfit;
    private List<PeriodValueElement> delegateCost;
    private List<SlashInfo> slash;
    public String toJSONString(){return JSON.toJSONString(this);}

    public List<PeriodValueElement> getStakeProfit() {
        return stakeProfit;
    }

    public void setStakeProfit(List<PeriodValueElement> stakeProfit) {
        this.stakeProfit = stakeProfit;
    }

    public List<PeriodValueElement> getStakeCost() {
        return stakeCost;
    }

    public void setStakeCost(List<PeriodValueElement> stakeCost) {
        this.stakeCost = stakeCost;
    }

    public List<PeriodValueElement> getDelegateProfit() {
        return delegateProfit;
    }

    public void setDelegateProfit(List<PeriodValueElement> delegateProfit) {
        this.delegateProfit = delegateProfit;
    }

    public List<PeriodValueElement> getDelegateCost() {
        return delegateCost;
    }

    public void setDelegateCost(List<PeriodValueElement> delegateCost) {
        this.delegateCost = delegateCost;
    }

    public List<SlashInfo> getSlash() {
        return slash;
    }

    public void setSlash(List<SlashInfo> slash) {
        this.slash = slash;
    }
}
