package com.platon.browserweb.common.req.risk;

import com.platon.browserweb.common.req.PageReq;

/**
 * @Author: luowei
 * @Date: 2018/8/27 13:54
 */
public class ListInvestorTradeRiskReq extends PageReq {

    /**
     * 计价货币
     */
    private String quoteCurrency;

    public String getQuoteCurrency() {
        return quoteCurrency;
    }

    public void setQuoteCurrency(String quoteCurrency) {
        this.quoteCurrency = quoteCurrency;
    }
}
