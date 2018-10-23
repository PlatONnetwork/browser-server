package com.platon.browserweb.common.req.order;

import java.util.Date;

/**
 * @Author: luowei
 * @Date: 2018/8/29 10:15
 */
public class ListOrderByIdsResp {

    /**
     * 委托单id
     */
    private String orderId;
    /**
     * 货币对
     */
    private String symbol;
    /**
     * 委托报价方式
     */
    private String orderType;
    /**
     * 方向
     */
    private String side;
    /**
     * 委托有效期规则
     */
    private Integer timeInForce;
    /**
     * 客户端发送时间（提币时间）
     */
    private Date clientSendTime;
    /**
     * 服务端接收时间
     */
    private Date serverReceiveTime;
    /**
     * 委托报价
     */
    private String orderPrice;
    /**
     * 委托数量
     */
    private String orderQty;
    /**
     * 委托金额
     */
    private String orderAmount;
    /**
     * 执行状态
     */
    private String execType;
    /**
     * 状态
     */
    private String ordStatus;
    /**
     * 订单被拒原因代码
     */
    private Integer order_reject_code;
    /**
     * 订单被拒原因
     */
    private String order_reject_reason;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Integer getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(Integer timeInForce) {
        this.timeInForce = timeInForce;
    }

    public Date getClientSendTime() {
        return clientSendTime;
    }

    public void setClientSendTime(Date clientSendTime) {
        this.clientSendTime = clientSendTime;
    }

    public Date getServerReceiveTime() {
        return serverReceiveTime;
    }

    public void setServerReceiveTime(Date serverReceiveTime) {
        this.serverReceiveTime = serverReceiveTime;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
    }

    public String getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(String orderAmount) {
        this.orderAmount = orderAmount;
    }

    public String getExecType() {
        return execType;
    }

    public void setExecType(String execType) {
        this.execType = execType;
    }

    public String getOrdStatus() {
        return ordStatus;
    }

    public void setOrdStatus(String ordStatus) {
        this.ordStatus = ordStatus;
    }

    public Integer getOrder_reject_code() {
        return order_reject_code;
    }

    public void setOrder_reject_code(Integer order_reject_code) {
        this.order_reject_code = order_reject_code;
    }

    public String getOrder_reject_reason() {
        return order_reject_reason;
    }

    public void setOrder_reject_reason(String order_reject_reason) {
        this.order_reject_reason = order_reject_reason;
    }
}
