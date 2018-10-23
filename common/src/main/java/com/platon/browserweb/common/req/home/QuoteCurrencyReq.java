package com.platon.browserweb.common.req.home;

import java.util.List;

/**
 * User: dongqile
 * Date: 2018/9/13
 * Time: 17:18
 */
public class QuoteCurrencyReq {
    /**
     * 代理商id
     */
    private long brokerId;

    /**
     * 计价币列表
     */
    private List<String> quoteCurrency;

    public long getBrokerId () {
        return brokerId;
    }

    public void setBrokerId ( long brokerId ) {
        this.brokerId = brokerId;
    }

    public List <String> getQuoteCurrency () {
        return quoteCurrency;
    }

    public void setQuoteCurrency ( List <String> quoteCurrency ) {
        this.quoteCurrency = quoteCurrency;
    }
}