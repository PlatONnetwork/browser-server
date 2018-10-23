package com.platon.browserweb.common.req.currency;

/**
 * @Author: luowei
 * @Date: 2018/9/13 11:20
 */
public class SetCurrencyReq {

    private String currency;
    private Boolean enable;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }
}
