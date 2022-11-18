package com.platon.browser.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class NodeAprBase {

    /**
     * 结算周期
     */
    private Integer epochNum;

    /**
     * 委托年化收益率
     */
    private String deleAnnualizedRate;

}
