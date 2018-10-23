package com.platon.browserweb.common.req.order;

import org.hibernate.validator.constraints.NotBlank;

/**
 * @Author: luowei
 * @Date: 2018/8/30 15:02
 */
public class CancelOrderReq {

    /**
     * 订单ID
     */
    @NotBlank
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
