package com.platon.browser.dto;

import lombok.Data;
import org.web3j.protocol.core.methods.response.Log;

import java.math.BigInteger;

/**
 * @program: browser-server
 * @description: 交易时间对象
 * @author: Rongjin Zhang
 * @create: 2020-09-23 12:02
 */
@Data
public class TransferEvent {
    private String from;
    private String to;
    private BigInteger value;
    private Log log;
}
