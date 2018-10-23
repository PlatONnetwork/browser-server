package com.platon.browserweb.common.dto;

import java.math.BigDecimal;

/**
 * User: dongqile
 * Date: 2018/6/29
 * Time: 16:27
 */
public class AssetsList {
    /*
    * 币种
    * */
    private String currency;

    /*
    * 资产总和
    * */
    private BigDecimal sum;

    public String getCurrency () {
        return currency;
    }

    public void setCurrency ( String currency ) {
        this.currency = currency;
    }

    public BigDecimal getSum () {
        return sum;
    }

    public void setSum ( BigDecimal sum ) {
        this.sum = sum;
    }
}