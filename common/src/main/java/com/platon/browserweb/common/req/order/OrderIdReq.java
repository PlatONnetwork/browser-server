package com.platon.browserweb.common.req.order;

import com.platon.browserweb.common.validate.OrderIdGroup;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * User: dongqile
 * Date: 2018/7/8
 * Time: 16:31
 */
public class OrderIdReq {

    @NotEmpty(groups = OrderIdGroup.class)
    private String orderId;

    private String type;

    public String getOrderId () {
        return orderId;
    }

    public void setOrderId ( String orderId ) {
        this.orderId = orderId;
    }

    public String getType () {
        return type;
    }

    public void setType ( String type ) {
        this.type = type;
    }
}