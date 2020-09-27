package com.platon.browser.dto;

import java.math.BigInteger;

import org.web3j.protocol.core.methods.response.Log;

import lombok.Data;

/**
 * @program: browser-server
 * @description: 交易时间对象
 * @author: Rongjin Zhang
 * @create: 2020-09-23 12:02
 */
@Data
public class TransferEvent {
    public String from;

    public String to;

    public BigInteger value;
    
    public Log log;
}
