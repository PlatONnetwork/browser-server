package com.platon.browser.dto.ticket;

import lombok.Data;

import java.math.BigInteger;

@Data
public class TxInfo{
    @Data
    public static class Parameter{
        BigInteger price;
        Integer count;
        String nodeId;
    }
    String functionName,type;
    Parameter parameters;
}