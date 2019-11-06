package com.platon.browser.complement.cache.bean;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * @Auther: Chendongming
 * @Date: 2019/8/21 15:09
 * @Description: 年化率信息bean
 */
@Data
public class AnnualizedRateInfo {
    private List<PeriodValueElement> profit = new ArrayList<>();
    private List<PeriodValueElement> cost = new ArrayList<>();
}
