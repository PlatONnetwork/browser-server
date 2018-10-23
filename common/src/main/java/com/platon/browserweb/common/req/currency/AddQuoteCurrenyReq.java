package com.platon.browserweb.common.req.currency;

import java.util.List;

/**
 * @Author: luowei
 * @Date: 2018/8/25 16:43
 */
public class AddQuoteCurrenyReq {

    /**
     * 添加的计价币列表
     */
    private List<String> quoteCurrencys;

    public List<String> getQuoteCurrencys() {
        return quoteCurrencys;
    }

    public void setQuoteCurrencys(List<String> quoteCurrencys) {
        this.quoteCurrencys = quoteCurrencys;
    }
}
