package com.platon.browserweb.common.req.order;

import com.platon.browserweb.common.req.PageReq;

import java.util.List;

/**
 * @Author: luowei
 * @Date: 2018/8/29 10:14
 */
public class ListOrderByIdsReq extends PageReq {

    /**
     * 订单ID
     */
    private List<String> orderId;

    public List<String> getOrderId() {
        return orderId;
    }

    public void setOrderId(List<String> orderId) {
        this.orderId = orderId;
    }
}
