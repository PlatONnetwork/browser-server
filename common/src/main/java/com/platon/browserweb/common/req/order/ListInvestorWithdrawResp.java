package com.platon.browserweb.common.req.order;

import java.util.Date;

/**
 * @Author: luowei
 * @Date: 2018/8/29 10:35
 */
public class ListInvestorWithdrawResp {

    /**
     * 委托单id
     */
    private String orderId;
    /**
     * 货币
     */
    private String currency;
    /**
     * 客户端发送时间（提币时间）
     */
    private Date clientSendTime;
    /**
     * 服务端接收时间
     */
    private Date serverReceiveTime;
    /**
     * 提币目的地址
     */
    private String toAddress;
    /**
     * 提币数量
     */
    private String orderQty;
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

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
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

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(String orderQty) {
        this.orderQty = orderQty;
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
