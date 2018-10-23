package com.platon.browserweb.common.req.currency;

import java.util.List;

/**
 * @Author: luowei
 * @Date: 2018/9/5 10:03
 */
public class AddCurrencyReq {

    /**
     * 添加的服务币列表
     */
    private List<String> currencys;

    public List<String> getCurrencys() {
        return currencys;
    }

    public void setCurrencys(List<String> currencys) {
        this.currencys = currencys;
    }
}
