package com.platon.browser.common.complement.dto;

import com.alibaba.fastjson.JSON;

import java.util.List;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/21 15:09
 * @Description: 年化率信息bean
 */
public class AnnualizedRateInfo {
    private List<PeriodValueElement> profit;
    private List<PeriodValueElement> cost;
    private List<SlashInfo> slash;
    public String toJSONString(){return JSON.toJSONString(this);}

    public List<PeriodValueElement> getProfit() {
        return profit;
    }

    public void setProfit(List<PeriodValueElement> profit) {
        this.profit = profit;
    }

    public List<PeriodValueElement> getCost() {
        return cost;
    }

    public void setCost(List<PeriodValueElement> cost) {
        this.cost = cost;
    }

    public List<SlashInfo> getSlash() {
        return slash;
    }

    public void setSlash(List<SlashInfo> slash) {
        this.slash = slash;
    }
}
