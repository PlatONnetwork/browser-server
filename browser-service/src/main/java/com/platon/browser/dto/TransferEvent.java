package com.platon.browser.dto;

import com.alaya.protocol.core.methods.response.Log;

import java.math.BigInteger;

/**
 * @program: browser-server
 * @description: 交易时间对象
 * @author: Rongjin Zhang
 * @create: 2020-09-23 12:02
 */
public class TransferEvent {
    private String from;
    private String to;
    private BigInteger value;
    private Log log;

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public BigInteger getValue() {
        return this.value;
    }

    public void setValue(BigInteger value) {
        this.value = value;
    }

    public Log getLog() {
        return this.log;
    }

    public void setLog(Log log) {
        this.log = log;
    }
}
