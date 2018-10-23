package com.platon.browserweb.common.req.risk;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Author: luowei
 * @Date: 2018/8/27 15:54
 */
public class SetInvestorEntrustPriceRiskReq {

    /**
     * 委托买价阈值
     */
    @NotBlank(message = "委托买价阈值不能为空")
    private String entrustBuyQty;
    /**
     * 委托卖价阈值
     */
    @NotBlank(message = "委托卖价阈值不能为空")
    private String entrustSellQty;

    public String getEntrustBuyQty() {
        return entrustBuyQty;
    }

    public void setEntrustBuyQty(String entrustBuyQty) {
        this.entrustBuyQty = entrustBuyQty;
    }

    public String getEntrustSellQty() {
        return entrustSellQty;
    }

    public void setEntrustSellQty(String entrustSellQty) {
        this.entrustSellQty = entrustSellQty;
    }
}
